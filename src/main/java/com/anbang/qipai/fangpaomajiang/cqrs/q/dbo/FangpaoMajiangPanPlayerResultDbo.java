package com.anbang.qipai.fangpaomajiang.cqrs.q.dbo;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanPlayerResult;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;

public class FangpaoMajiangPanPlayerResultDbo {
	private String playerId;
	private FangpaoMajiangPanPlayerResult playerResult;
	private MajiangPlayerValueObject player;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public FangpaoMajiangPanPlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(FangpaoMajiangPanPlayerResult playerResult) {
		this.playerResult = playerResult;
	}

	public MajiangPlayerValueObject getPlayer() {
		return player;
	}

	public void setPlayer(MajiangPlayerValueObject player) {
		this.player = player;
	}

}
