package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.listener.mo.MajiangPlayerMoActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;

/**
 * 放炮麻将记录最后摸牌玩家
 * 
 * @author lsc
 *
 */
public class FangpaoMajiangLastMoActionPlayerRecorder implements MajiangPlayerMoActionStatisticsListener {

	private String lastMoActionPlayerId;

	@Override
	public void updateForNextPan() {

	}

	@Override
	public void update(MajiangMoAction moAction, Ju ju) {
		setLastMoActionPlayerId(moAction.getActionPlayerId());
	}

	public String getLastMoActionPlayerId() {
		return lastMoActionPlayerId;
	}

	public void setLastMoActionPlayerId(String lastMoActionPlayerId) {
		this.lastMoActionPlayerId = lastMoActionPlayerId;
	}

}
