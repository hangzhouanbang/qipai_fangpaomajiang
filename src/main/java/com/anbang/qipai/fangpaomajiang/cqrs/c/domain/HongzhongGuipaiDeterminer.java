package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.guipai.GuipaiDeterminer;
import com.dml.majiang.player.MajiangPlayer;

/**
 * 红中是鬼牌
 * 
 * @author lsc
 *
 */
public class HongzhongGuipaiDeterminer implements GuipaiDeterminer {

	public HongzhongGuipaiDeterminer() {
	}

	@Override
	public void determineGuipai(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<MajiangPai> paiTypeList = currentPan.getPaiTypeList();
		if (paiTypeList.contains(MajiangPai.hongzhong)) {
			MajiangPai guipaiType = MajiangPai.hongzhong;
			currentPan.getPublicGuipaiSet().add(guipaiType);
			for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
				majiangPlayer.addGuipaiType(guipaiType);
			}
		}
	}

}
