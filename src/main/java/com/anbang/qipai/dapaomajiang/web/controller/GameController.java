package com.anbang.qipai.dapaomajiang.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.dapaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.dapaomajiang.cqrs.c.domain.VoteToFinishResult;
import com.anbang.qipai.dapaomajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.dapaomajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.dapaomajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.dapaomajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.dapaomajiang.msg.service.FangpaoMajiangGameMsgService;
import com.anbang.qipai.dapaomajiang.web.vo.CommonVO;
import com.anbang.qipai.dapaomajiang.web.vo.GameVO;
import com.anbang.qipai.dapaomajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.dapaomajiang.websocket.QueryScope;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private PlayerAuthService playerAuthService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private FangpaoMajiangGameMsgService gameMsgService;

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@RequestMapping("/newgame")
	public CommonVO newgame(String playerId, int difen, int taishu, int panshu, int renshu, boolean dapao) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		gameCmdService.newMajiangGame(newGameId, playerId, difen, taishu, panshu, renshu, dapao);
		majiangGameQueryService.newMajiangGame(newGameId, playerId, difen, taishu, panshu, renshu, dapao);
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	@RequestMapping("/joingame")
	public CommonVO joingame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		GameValueObject gameValueObject;
		try {
			gameValueObject = gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		majiangGameQueryService.joinGame(gameId, playerId);
		// 通知其他人
		for (String otherPlayerId : gameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		}

		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);

		vo.setData(data);
		return vo;
	}

	@RequestMapping("/leavegame")
	public CommonVO leavegame(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		wsNotifier.closeSessionForPlayer(playerId);
		GameValueObject gameValueObject;
		try {
			gameValueObject = gameCmdService.leaveGame(playerId);
			if (gameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.leaveGame(gameValueObject);
		gameMsgService.gamePlayerLeave(gameValueObject, playerId);
		// 通知其他玩家
		List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService
				.findGamePlayerDbosForGame(gameValueObject.getId());
		gamePlayerDboList.forEach((gamePlayerDbo) -> {
			String otherPlayerId = gamePlayerDbo.getPlayerId();
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		});
		return vo;
	}

	@RequestMapping("/backtogame")
	public CommonVO backtogame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		try {
			gameCmdService.backToGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}

		majiangGameQueryService.backToGame(playerId, gameId);

		// 通知其他玩家
		List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService.findGamePlayerDbosForGame(gameId);
		gamePlayerDboList.forEach((gamePlayerDbo) -> {
			String otherPlayerId = gamePlayerDbo.getPlayerId();
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		});

		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	@RequestMapping("/info")
	public CommonVO info(String gameId) {
		CommonVO vo = new CommonVO();
		MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
		List<MajiangGamePlayerDbo> gamePlayerDboListForGameId = majiangGameQueryService
				.findGamePlayerDbosForGame(gameId);
		GameVO gameVO = new GameVO(majiangGameDbo, gamePlayerDboListForGameId);
		Map data = new HashMap();
		data.put("game", gameVO);
		vo.setData(data);
		return vo;
	}

	@RequestMapping("/ready")
	public CommonVO ready(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		ReadyForGameResult readyForGameResult;
		try {
			readyForGameResult = gameCmdService.readyForGame(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			majiangPlayQueryService.readyForGame(readyForGameResult);
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		// 通知其他人
		for (String otherPlayerId : readyForGameResult.getOtherPlayerIds()) {
			wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			if (readyForGameResult.getGame().getState().equals(GameState.playing)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.panForMe.name());
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (readyForGameResult.getGame().getState().equals(GameState.playing)) {
			queryScopes.add(QueryScope.panForMe);
		}
		data.put("queryScopes", queryScopes);
		return vo;
	}

	@RequestMapping("/launch_finish_vote")
	public CommonVO launchfinishvote(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		VoteToFinishResult voteToFinishResult;
		try {
			voteToFinishResult = gameCmdService.launchFinishVote(playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}
}
