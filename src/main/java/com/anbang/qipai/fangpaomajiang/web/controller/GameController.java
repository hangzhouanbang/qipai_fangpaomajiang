package com.anbang.qipai.fangpaomajiang.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.fangpaomajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.fangpaomajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.fangpaomajiang.msg.service.FangpaoMajiangGameMsgService;
import com.anbang.qipai.fangpaomajiang.msg.service.FangpaoMajiangResultMsgService;
import com.anbang.qipai.fangpaomajiang.web.vo.CommonVO;
import com.anbang.qipai.fangpaomajiang.web.vo.GameVO;
import com.anbang.qipai.fangpaomajiang.web.vo.JuResultVO;
import com.anbang.qipai.fangpaomajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.fangpaomajiang.websocket.QueryScope;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.GameNotFoundException;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private PlayerAuthService playerAuthService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private FangpaoMajiangGameMsgService gameMsgService;

	@Autowired
	private FangpaoMajiangResultMsgService fangpaoMajiangResultMsgService;

	/**
	 * 新一局游戏
	 */
	@RequestMapping(value = "/newgame")
	@ResponseBody
	public CommonVO newgame(String playerId, int panshu, int renshu, boolean hongzhongcaishen, boolean dapao,
			boolean sipaofanbei, boolean zhuaniao, int niaoshu) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		MajiangGameValueObject majiangGameValueObject = gameCmdService.newMajiangGame(newGameId, playerId, panshu,
				renshu, hongzhongcaishen, dapao, sipaofanbei, zhuaniao, niaoshu);
		majiangGameQueryService.newMajiangGame(majiangGameValueObject);
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 加入游戏
	 */
	@RequestMapping(value = "/joingame")
	@ResponseBody
	public CommonVO joingame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		majiangGameQueryService.joinGame(majiangGameValueObject);
		// 通知其他人
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId)).forEach((scope) -> {
							wsNotifier.notifyToQuery(otherPlayerId, scope.name());
						});
			}
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 挂起（手机按黑的时候调用）
	 */
	@RequestMapping(value = "/hangup")
	@ResponseBody
	public CommonVO hangup(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.leaveGameByHangup(playerId);
			if (majiangGameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.leaveGame(majiangGameValueObject);
		// 断开玩家的socket
		wsNotifier.closeSessionForPlayer(playerId);
		gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);
		// 通知其他玩家
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				if (majiangGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
						|| majiangGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
					scopes.remove(QueryScope.gameFinishVote);
				}
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}
		return vo;
	}

	/**
	 * 离开游戏(非退出,还会回来的)
	 */
	@RequestMapping(value = "/leavegame")
	@ResponseBody
	public CommonVO leavegame(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.leaveGame(playerId);
			if (majiangGameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.leaveGame(majiangGameValueObject);
		// 断开玩家的socket
		wsNotifier.closeSessionForPlayer(playerId);
		gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);
		// 通知其他玩家
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				if (majiangGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
						|| majiangGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
					scopes.remove(QueryScope.gameFinishVote);
				}
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}
		return vo;
	}

	/**
	 * 返回游戏
	 */
	@RequestMapping(value = "/backtogame")
	@ResponseBody
	public CommonVO backtogame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.backToGame(playerId, gameId);
		} catch (Exception e) {
			// 如果找不到game，看下是否是已经结束(正常结束和被投票)的game
			if (e instanceof GameNotFoundException) {
				MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
				if (majiangGameDbo != null && (majiangGameDbo.getState().name().equals(FinishedByVote.name)
						|| majiangGameDbo.getState().name().equals(Finished.name))) {
					data.put("queryScope", QueryScope.juResult);
					return vo;
				}
			}
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}

		majiangGameQueryService.backToGame(playerId, majiangGameValueObject);

		// 通知其他人
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				scopes.remove(QueryScope.gameFinishVote);
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}

		String token = playerAuthService.newSessionForPlayer(playerId);
		data.put("token", token);
		return vo;
	}

	/**
	 * 游戏的所有信息,不包含局
	 * 
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public CommonVO info(String gameId) {
		CommonVO vo = new CommonVO();
		MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
		GameVO gameVO = new GameVO(majiangGameDbo);
		Map data = new HashMap();
		data.put("game", gameVO);
		vo.setData(data);
		return vo;
	}

	/**
	 * 最开始的准备,不适用下一盘的准备
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/ready")
	@ResponseBody
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
			majiangPlayQueryService.readyForGame(readyForGameResult);// TODO 一起点准备的时候可能有同步问题.要靠框架解决
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		// 通知其他人
		for (String otherPlayerId : readyForGameResult.getMajiangGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				QueryScope.scopesForState(readyForGameResult.getMajiangGame().getState(),
						readyForGameResult.getMajiangGame().findPlayerState(otherPlayerId)).forEach((scope) -> {
							wsNotifier.notifyToQuery(otherPlayerId, scope.name());
						});
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (readyForGameResult.getMajiangGame().getState().name().equals(Playing.name)) {
			queryScopes.add(QueryScope.panForMe);
		}
		data.put("queryScopes", queryScopes);
		return vo;
	}

	@RequestMapping(value = "/finish")
	@ResponseBody
	public CommonVO finish(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.finish(playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.finish(majiangGameValueObject);
		String gameId = majiangGameValueObject.getId();
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, majiangGameDbo);
			fangpaoMajiangResultMsgService.recordJuResult(juResult);
		}

		if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
				|| majiangGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			data.put("queryScope", QueryScope.gameInfo);
		} else {
			// 游戏没结束有两种可能：一种是发起了投票。还有一种是游戏没开始，解散发起人又不是房主，那就自己走人。
			if (majiangGameValueObject.allPlayerIds().contains(playerId)) {
				data.put("queryScope", QueryScope.gameFinishVote);
			} else {
				data.put("queryScope", null);
			}
		}
		// 通知其他人来查询
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}

		return vo;
	}

	@RequestMapping(value = "/vote_to_finish")
	@ResponseBody
	public CommonVO votetofinish(String token, boolean yes) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.voteToFinish(playerId, yes);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String gameId = majiangGameValueObject.getId();
		majiangGameQueryService.voteToFinish(majiangGameValueObject);
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, majiangGameDbo);
			fangpaoMajiangResultMsgService.recordJuResult(juResult);
			gameMsgService.gameFinished(gameId);
		}

		data.put("queryScope", QueryScope.gameFinishVote);
		// 通知其他人来查询投票情况
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState(),
						majiangGameValueObject.findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}
		return vo;
	}

	@RequestMapping(value = "/finish_vote_info")
	@ResponseBody
	public CommonVO finishvoteinfo(String gameId) {

		CommonVO vo = new CommonVO();
		GameFinishVoteDbo gameFinishVoteDbo = majiangGameQueryService.findGameFinishVoteDbo(gameId);
		Map data = new HashMap();
		data.put("vote", gameFinishVoteDbo.getVote());
		vo.setData(data);
		return vo;
	}
}
