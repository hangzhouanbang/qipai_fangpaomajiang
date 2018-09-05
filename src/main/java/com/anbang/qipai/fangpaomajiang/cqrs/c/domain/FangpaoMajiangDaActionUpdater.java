package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.DianpaoDihuOpportunityDetector;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

/**
 * 一炮多响的打
 * 
 * @author lsc
 *
 */
public class FangpaoMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
		// 是否可以地胡
		DianpaoDihuOpportunityDetector dianpaoDihuOpportunityDetector = ju.getActionStatisticsListenerManager()
				.findListener(DianpaoDihuOpportunityDetector.class);
		boolean couldDihu = dianpaoDihuOpportunityDetector.ifDihuOpportunity();
		daPlayer.clearActionCandidates();

		MajiangPlayer xiajiaPlayer = currentPan.findXiajia(daPlayer);
		xiajiaPlayer.clearActionCandidates();
		// 下家可以吃
		xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getActionPlayerId(), daAction.getPai());
		while (true) {
			if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
				// 其他的可以碰杠胡
				xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				// 点炮胡
				GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
				// 先把这张牌放入计算器
				xiajiaPlayer.getShoupaiCalculator().addPai(daAction.getPai());
				FangpaoMajiangHu bestHu = FangpaoMajiangJiesuanCalculator.calculateBestDianpaoHu(couldDihu,
						gouXingPanHu, xiajiaPlayer, daAction.getPai());
				// 再把这张牌拿出计算器
				xiajiaPlayer.getShoupaiCalculator().removePai(daAction.getPai());
				if (bestHu != null) {
					bestHu.setDianpao(true);
					bestHu.setDianpaoPlayerId(daPlayer.getId());
					xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
				}

				xiajiaPlayer.checkAndGenerateGuoCandidateAction();
			} else {
				break;
			}
			xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
			xiajiaPlayer.clearActionCandidates();
		}

		// 如果所有玩家啥也做不了,那就下家摸牌
		if (currentPan.allPlayerHasNoActionCandidates()) {
			xiajiaPlayer = currentPan.findXiajia(daPlayer);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}

		// TODO 接着做

	}

}
