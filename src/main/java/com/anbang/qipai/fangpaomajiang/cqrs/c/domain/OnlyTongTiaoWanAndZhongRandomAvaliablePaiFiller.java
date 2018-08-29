package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.avaliablepai.AvaliablePaiFiller;

public class OnlyTongTiaoWanAndZhongRandomAvaliablePaiFiller implements AvaliablePaiFiller {

	private long seed;

	public OnlyTongTiaoWanAndZhongRandomAvaliablePaiFiller() {
	}

	public OnlyTongTiaoWanAndZhongRandomAvaliablePaiFiller(long seed) {
		this.seed = seed;
	}

	@Override
	public void fillAvaliablePai(Ju ju) throws Exception {
		Set<MajiangPai> notPlaySet = new HashSet<>();
		notPlaySet.add(MajiangPai.chun);
		notPlaySet.add(MajiangPai.xia);
		notPlaySet.add(MajiangPai.qiu);
		notPlaySet.add(MajiangPai.dong);
		notPlaySet.add(MajiangPai.mei);
		notPlaySet.add(MajiangPai.lan);
		notPlaySet.add(MajiangPai.zhu);
		notPlaySet.add(MajiangPai.ju);
		notPlaySet.add(MajiangPai.dongfeng);
		notPlaySet.add(MajiangPai.nanfeng);
		notPlaySet.add(MajiangPai.xifeng);
		notPlaySet.add(MajiangPai.beifeng);
		notPlaySet.add(MajiangPai.facai);
		notPlaySet.add(MajiangPai.baiban);
		MajiangPai[] allMajiangPaiArray = MajiangPai.values();
		List<MajiangPai> playPaiTypeList = new ArrayList<>();
		for (int i = 0; i < allMajiangPaiArray.length; i++) {
			MajiangPai pai = allMajiangPaiArray[i];
			if (!notPlaySet.contains(pai)) {
				playPaiTypeList.add(pai);
			}
		}

		List<MajiangPai> allPaiList = new ArrayList<>();
		playPaiTypeList.forEach((paiType) -> {
			for (int i = 0; i < 4; i++) {
				allPaiList.add(paiType);
			}
		});

		Collections.shuffle(allPaiList, new Random(seed));
		ju.getCurrentPan().setAvaliablePaiList(allPaiList);
		ju.getCurrentPan().setPaiTypeList(playPaiTypeList);
		seed++;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
