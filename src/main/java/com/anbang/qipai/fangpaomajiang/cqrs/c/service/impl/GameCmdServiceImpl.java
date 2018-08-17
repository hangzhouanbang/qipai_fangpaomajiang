package com.anbang.qipai.fangpaomajiang.cqrs.c.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangJuResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.VoteToFinishResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.service.GameCmdService;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.finish.GameFinishVoteValueObject;
import com.dml.mpgame.game.finish.MostPlayersWinVoteCalculator;
import com.dml.mpgame.game.join.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.game.leave.HostGameLeaveStrategy;
import com.dml.mpgame.game.ready.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.server.GameServer;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.playerCreateGame(gameId, new FixedNumberOfPlayersGameJoinStrategy(renshu),
				new FixedNumberOfPlayersGameReadyStrategy(renshu), new HostGameLeaveStrategy(playerId), playerId);
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		majiangGameManager.newMajiangGame(gameId, difen, taishu, panshu, renshu, dapao);
	}

	@Override
	public GameValueObject joinGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.join(playerId, gameId);
	}

	@Override
	public void bindPlayer(String playerId, String gameId) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.bindPlayer(playerId, gameId);
	}

	@Override
	public GameValueObject leaveGame(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.leave(playerId);
	}

	@Override
	public GameValueObject backToGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.back(playerId, gameId);
	}

	@Override
	public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
		ReadyForGameResult result = new ReadyForGameResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		GameValueObject gameValueObject = gameServer.ready(playerId);
		result.setGame(gameValueObject);

		List<String> playerIds = gameServer.findAllPlayerIdsForGame(gameValueObject.getId());
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);

		if (gameValueObject.getState().equals(GameState.playing)) {
			MajiangGameManager majiangManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
			PanActionFrame firstActionFrame = majiangManager.createJuAndStartFirstPan(gameValueObject, currentTime);
			result.setFirstActionFrame(firstActionFrame);
		}
		return result;
	}

	@Override
	public VoteToFinishResult launchFinishVote(String playerId) throws Exception {
		VoteToFinishResult result = new VoteToFinishResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		GameFinishVoteValueObject voteValueObject = gameServer.startFinishVote(playerId,
				new MostPlayersWinVoteCalculator());
		result.setVoteValueObject(voteValueObject);
		// 有可能发起投票就解散
		if (voteValueObject.getResult() != null) {
			MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
			FangpaoMajiangJuResult juResult = majiangGameManager.finishMajiangGame(voteValueObject.getGameId());
			result.setJuResult(juResult);
		}
		return result;
	}

}
