package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.listener.FangpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.HuFirstException;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;

/**
 * 碰的那个人要打牌
 * 
 * @author lsc
 *
 */
public class FangpaoMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		FangpaoMajiangPengGangActionStatisticsListener fangpaoMajiangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(FangpaoMajiangPengGangActionStatisticsListener.class);
		if (fangpaoMajiangStatisticsListener.getPlayerActionMap().containsKey(pengAction.getActionPlayerId())) {
			throw new HuFirstException();
		} else {
			Pan currentPan = ju.getCurrentPan();
			currentPan.clearAllPlayersActionCandidates();
			MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
