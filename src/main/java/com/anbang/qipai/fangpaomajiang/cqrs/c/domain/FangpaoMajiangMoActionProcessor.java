package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionProcessor;
import com.dml.majiang.player.shoupai.ShoupaiDuiziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class FangpaoMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		currentPan.playerMoPai(action.getActionPlayerId());
		currentPan.setActivePaiCursor(null);

		MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());
		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();
		MajiangPai[] guipaiTypes = new MajiangPai[guipaiTypeSet.size()];
		guipaiTypeSet.toArray(guipaiTypes);
		if (guipaiTypeSet.contains(gangmoShoupai) && fangruShoupaiList.size() == 0) {
			// 胡
			FangpaoMajiangHu bestHu = new FangpaoMajiangHu();
			FangpaoMajiangHufen hufen = new FangpaoMajiangHufen();
			hufen.setCaishendiao(true);
			hufen.setDanzhangdiao(true);
			hufen.setHu(true);
			hufen.setZimoHu(true);
			hufen.calculate();
			bestHu.setHuxingHu(true);
			bestHu.setZimo(true);
			bestHu.setHufen(hufen);
			ShoupaiPaiXing shoupaiPaiXing = new ShoupaiPaiXing();
			List<ShoupaiDuiziZu> duiziList = new ArrayList<>();
			ShoupaiDuiziZu duiziZu = new ShoupaiDuiziZu();
			MajiangPai guipaiType = guipaiTypes[0];
			duiziZu.setDuiziType(guipaiType);
			duiziZu.fillAllBlankPaiWithBenPai();
			duiziList.add(duiziZu);
			shoupaiPaiXing.setDuiziList(duiziList);
			bestHu.setShoupaiPaiXing(shoupaiPaiXing);
			player.setHu(bestHu);
		}
	}

}
