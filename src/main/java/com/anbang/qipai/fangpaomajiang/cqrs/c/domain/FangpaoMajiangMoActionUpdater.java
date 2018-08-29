package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.gang.GangCounter;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class FangpaoMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		int liupai = 14;
		GangCounter gangCounter = ju.getActionStatisticsListenerManager().findListener(GangCounter.class);
		if (gangCounter.getCount() > 0) {
			liupai += (4 + (gangCounter.getCount() - 1) * 2);
		}
		Pan currentPan = ju.getCurrentPan();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		player.clearActionCandidates();
		int avaliablePaiLeft = currentPan.countAvaliablePai();
		if (avaliablePaiLeft - liupai == 0) {// 没牌了
			// 当然啥也不干了
		} else {
			// 有手牌或刻子可以杠这个摸来的牌
			player.tryShoupaigangmoAndGenerateCandidateAction();
			player.tryKezigangmoAndGenerateCandidateAction();

			// 杠四个手牌
			player.tryGangsigeshoupaiAndGenerateCandidateAction();

			// 刻子杠手牌
			player.tryKezigangshoupaiAndGenerateCandidateAction();

			// 胡
			FangpaoMajiangPanResultBuilder fangpaoMajiangPanResultBuilder = (FangpaoMajiangPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();

			// 天胡
			boolean couldTianhu = false;
			if (currentPan.getZhuangPlayerId().equals(player.getId())) {
				if (player.countFangruShoupai() == 0) {
					couldTianhu = true;
				}
			}
//			FangpaoMajiangHu bestHu = FangpaoMajiangJiesuanCalculator.calculateBestZimoHu(couldTianhu, gouXingPanHu,
//					player, moAction, baibanIsGuipai);
//			if (bestHu != null) {
//				bestHu.setZimo(true);
//				player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
//			}
		}

	}

}
