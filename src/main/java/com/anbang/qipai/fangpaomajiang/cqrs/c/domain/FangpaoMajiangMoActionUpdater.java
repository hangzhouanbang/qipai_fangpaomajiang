package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.TianHuAndDihuOpportunityDetector;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class FangpaoMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		currentPan.clearAllPlayersActionCandidates();
		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		// 有手牌或刻子可以杠这个摸来的牌
		player.tryShoupaigangmoAndGenerateCandidateAction();
		player.tryKezigangmoAndGenerateCandidateAction();

		// 杠四个手牌
		player.tryGangsigeshoupaiAndGenerateCandidateAction();

		// 刻子杠手牌
		player.tryKezigangshoupaiAndGenerateCandidateAction();
		// 胡
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();

		// 天胡
		TianHuAndDihuOpportunityDetector tianHuAndDihuOpportunityDetector = ju.getActionStatisticsListenerManager()
				.findListener(TianHuAndDihuOpportunityDetector.class);
		boolean couldTianhu = false;
		if (currentPan.getZhuangPlayerId().equals(player.getId())) {
			if (tianHuAndDihuOpportunityDetector.ifTianhuOpportunity()) {
				couldTianhu = true;
			}
		}
		FangpaoMajiangHu bestHu = FangpaoMajiangJiesuanCalculator.calculateBestZimoHu(couldTianhu, gouXingPanHu, player,
				moAction);
		if (bestHu != null) {
			bestHu.setZimo(true);
			player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
		}
		if (guipaiTypeSet.contains(gangmoShoupai) && fangruShoupaiList.size() == 0) {
			// 当手上没有除鬼牌之外的牌时不能过
		} else {
			// 需要有“过”
			player.checkAndGenerateGuoCandidateAction();
		}
		// 啥也不能干，那只能打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

	}

}
