package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.listener.FangpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
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
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		if (pengAction.isDisabledByHigherPriorityAction()) {// 如果动作被阻塞
			player.clearActionCandidates();// 玩家已经做了决定，要删除动作
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 所有玩家行牌结束，并且没人胡
				FangpaoMajiangPengGangActionStatisticsListener pengGangRecordListener = ju
						.getActionStatisticsListenerManager()
						.findListener(FangpaoMajiangPengGangActionStatisticsListener.class);
				MajiangPlayerAction finallyDoneAction = pengGangRecordListener.findPlayerFinallyDoneAction();// 找出最终应该执行的动作
				if (finallyDoneAction != null) {
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰，也只能是碰
						MajiangPengAction action = (MajiangPengAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangPengAction(action.getActionPlayerId(),
								action.getDachupaiPlayerId(), action.getPai()));
					}
				} else {

				}
				pengGangRecordListener.updateForNextLun();// 清空动作缓存
			}
		} else {
			currentPan.clearAllPlayersActionCandidates();
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
