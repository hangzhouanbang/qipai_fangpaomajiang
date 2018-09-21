package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.listener.FangpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionUpdater;

/**
 * 吃的那个人要打牌
 * 
 * @author lsc
 *
 */
public class FangpaoMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater {

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		FangpaoMajiangPengGangActionStatisticsListener fangpaoMajiangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(FangpaoMajiangPengGangActionStatisticsListener.class);
		if (fangpaoMajiangStatisticsListener.getPlayerActionMap().containsKey(chiAction.getActionPlayerId())) {

		} else {
			Pan currentPan = ju.getCurrentPan();
			currentPan.clearAllPlayersActionCandidates();

			MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());

			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
