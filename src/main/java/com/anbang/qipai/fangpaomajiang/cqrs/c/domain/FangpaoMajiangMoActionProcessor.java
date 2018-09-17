package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionProcessor;

public class FangpaoMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		currentPan.playerMoPai(action.getActionPlayerId());
		currentPan.setActivePaiCursor(null);

		// MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());
		// List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		// MajiangPai gangmoShoupai = player.getGangmoShoupai();
		// Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();
		// MajiangPai[] guipaiTypes = new MajiangPai[guipaiTypeSet.size()];
		// guipaiTypeSet.toArray(guipaiTypes);
		// if (guipaiTypeSet.contains(gangmoShoupai) && fangruShoupaiList.size() == 0) {
		// // 胡
		// FangpaoMajiangHu bestHu = new FangpaoMajiangHu();
		// FangpaoMajiangHufen hufen = new FangpaoMajiangHufen();
		// hufen.setCaishendiao(true);
		// hufen.setDanzhangdiao(true);
		// hufen.setHu(true);
		// hufen.setZimoHu(true);
		// hufen.calculate();
		// bestHu.setHuxingHu(true);
		// bestHu.setZimo(true);
		// bestHu.setHufen(hufen);
		// ShoupaiPaiXing shoupaiPaiXing = new ShoupaiPaiXing();
		// List<ShoupaiDuiziZu> duiziList = new ArrayList<>();
		// ShoupaiDuiziZu duiziZu = new ShoupaiDuiziZu();
		// MajiangPai guipaiType = guipaiTypes[0];
		// duiziZu.setDuiziType(guipaiType);
		// GuipaiDangPai pai1 = new GuipaiDangPai();
		// pai1.setGuipai(guipaiType);
		// pai1.setDangpai(guipaiType);
		// duiziZu.setPai1(pai1);
		// GuipaiDangPai pai2 = new GuipaiDangPai();
		// pai2.setGuipai(guipaiType);
		// pai2.setDangpai(guipaiType);
		// duiziZu.setPai1(pai2);
		// duiziList.add(duiziZu);
		// shoupaiPaiXing.setDuiziList(duiziList);
		// List<ShoupaiDanpai> danpaiList = new ArrayList<>();
		// shoupaiPaiXing.setDanpaiList(danpaiList);
		// List<ShoupaiKeziZu> keziList = new ArrayList<>();
		// shoupaiPaiXing.setKeziList(keziList);
		// List<ShoupaiGangziZu> gangziList = new ArrayList<>();
		// shoupaiPaiXing.setGangziList(gangziList);
		// List<ShoupaiShunziZu> shunziList = new ArrayList<>();
		// shoupaiPaiXing.setShunziList(shunziList);
		// bestHu.setShoupaiPaiXing(shoupaiPaiXing);
		// player.setHu(bestHu);
		// }
	}

}
