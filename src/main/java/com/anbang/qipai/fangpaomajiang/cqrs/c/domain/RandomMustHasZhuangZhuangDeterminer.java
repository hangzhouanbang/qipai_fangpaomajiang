package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Random;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.zhuang.ZhuangDeterminer;

public class RandomMustHasZhuangZhuangDeterminer implements ZhuangDeterminer {

	private long seed;

	public RandomMustHasZhuangZhuangDeterminer() {
	}

	public RandomMustHasZhuangZhuangDeterminer(long seed) {
		this.seed = seed;
	}

	@Override
	public void determineZhuang(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<String> sortedPlayerIdList = currentPan.sortedPlayerIdList();

		Random r = new Random(seed);
		String zhuangPlayerId = sortedPlayerIdList.get(r.nextInt(sortedPlayerIdList.size()));
		currentPan.setZhuangPlayerId(zhuangPlayerId);
	}

}
