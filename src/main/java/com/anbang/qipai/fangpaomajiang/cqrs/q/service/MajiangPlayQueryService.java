package com.anbang.qipai.fangpaomajiang.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameState;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GamePlayerState;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;

public class MajiangPlayQueryService {

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		GameValueObject gameValueObject = readyForGameResult.getGame();
		gameValueObject.getPlayers().forEach((player) -> {
			if (player.getState().equals(GamePlayerState.readyToStart)) {
				gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), MajiangGamePlayerState.readyToStart);
			}
		});
		if (gameValueObject.getState().equals(GameState.playing)) {
			majiangGameDboDao.update(gameValueObject.getId(), MajiangGameState.playing);
			gamePlayerDboDao.updatePlayersStateForGame(gameValueObject.getId(), MajiangGamePlayerState.playing);
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			majiangGameDboDao.update(gameValueObject.getId(), panActionFrame.toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		}
	}
}
