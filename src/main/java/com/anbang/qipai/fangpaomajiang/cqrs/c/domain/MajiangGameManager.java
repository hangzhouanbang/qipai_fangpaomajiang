package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GameValueObject;

public class MajiangGameManager {

	private Map<String, MajiangGame> gameIdMajiangGameMap = new HashMap<>();

	public MajiangGameValueObject newMajiangGame(GameValueObject gameValueObject, int difen, int taishu, int panshu,
			int renshu, boolean dapao) {
		String gameId = gameValueObject.getId();
		MajiangGame majiangGame = new MajiangGame();
		majiangGame.setDapao(dapao);
		majiangGame.setDifen(difen);
		majiangGame.setPanshu(panshu);
		majiangGame.setRenshu(renshu);
		majiangGame.setTaishu(taishu);
		majiangGame.setGameId(gameId);
		majiangGame.updateByGame(gameValueObject);
		gameIdMajiangGameMap.put(gameId, majiangGame);
		return new MajiangGameValueObject(majiangGame);
	}

	public PanActionFrame createJuAndStartFirstPan(GameValueObject game, long currentTime) throws Exception {
		MajiangGame majiangGame = gameIdMajiangGameMap.get(game.getId());
		return majiangGame.createJuAndStartFirstPan(game, currentTime);
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

	public MajiangGameValueObject updateMajiangGameByGame(GameValueObject game) {
		MajiangGame majiangGame = gameIdMajiangGameMap.get(game.getId());
		return majiangGame.updateByGame(game);
	}

	public FangpaoMajiangJuResult finishMajiangGame(String gameId) {
		MajiangGame game = gameIdMajiangGameMap.remove(gameId);
		if (game.getJu() != null) {
			return (FangpaoMajiangJuResult) game.finishJu();
		} else {
			return null;
		}
	}
}
