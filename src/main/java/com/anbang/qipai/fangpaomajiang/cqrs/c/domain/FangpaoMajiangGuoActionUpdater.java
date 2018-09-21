package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.listener.FangpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;

public class FangpaoMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());

		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠
			// 那要我打牌
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
				FangpaoMajiangPengGangActionStatisticsListener pengGangRecordListener = ju
						.getActionStatisticsListenerManager()
						.findListener(FangpaoMajiangPengGangActionStatisticsListener.class);
				MajiangPlayerAction finallyDoneAction = pengGangRecordListener.findPlayerFinallyDoneAction();
				if (finallyDoneAction != null) {// 有其他吃碰杠动作，先执行吃碰杠
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {
						actionPlayer.addActionCandidate((MajiangPengAction) finallyDoneAction);
					} else if (finallyDoneAction instanceof MajiangGangAction) {
						actionPlayer.addActionCandidate((MajiangGangAction) finallyDoneAction);
					}
					pengGangRecordListener.updateForNextLun();
				} else {
					// 打牌那家的下家摸牌
					MajiangPlayer xiajiaPlayer = currentPan
							.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
					xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
				}
			}
		} else if (action.getType().equals(MajiangPlayerActionType.gang)) {// 过的是别人杠牌之后我可以胡
			if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
				// 杠牌那家摸牌
				MajiangPlayer gangPlayer = currentPan.findPlayerById(action.getActionPlayerId());
				gangPlayer.addActionCandidate(new MajiangMoAction(gangPlayer.getId(), new LundaoMopai()));
			}
		} else {

		}
	}

}
