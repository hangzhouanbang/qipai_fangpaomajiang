package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GameValueObject;

public class MajiangGameManager {

	private Map<String, MajiangGame> gameIdMajiangGameMap = new HashMap<>();

	public void newMajiangGame(String gameId, int difen, int taishu, int panshu, int renshu, boolean dapao) {
		MajiangGame majiangGame = new MajiangGame();
		majiangGame.setDapao(dapao);
		majiangGame.setDifen(difen);
		majiangGame.setPanshu(panshu);
		majiangGame.setRenshu(renshu);
		majiangGame.setTaishu(taishu);
		majiangGame.setGameId(gameId);
		gameIdMajiangGameMap.put(gameId, majiangGame);
	}

	public PanActionFrame createJuAndStartFirstPan(GameValueObject game, long currentTime) throws Exception {
		MajiangGame majiangGame = gameIdMajiangGameMap.get(game.getId());
		return majiangGame.createJuAndStartFirstPan(game, currentTime);
	}

	public FangpaoMajiangJuResult finishMajiangGame(String gameId) {
		MajiangGame majiangGame = gameIdMajiangGameMap.get(gameId);
		return (FangpaoMajiangJuResult) majiangGame.finishJu();
	}
}
