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
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		if (fangpaoMajiangStatisticsListener.getPlayerActionMap().containsKey(player.getId())) {
			player.clearActionCandidates();// 玩家已经做了决定，要删除动作
			throw new HuFirstException();
		} else {
			currentPan.clearAllPlayersActionCandidates();

			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
