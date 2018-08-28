package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.zhuang.ZhuangDeterminer;

public class DianpaoZhuangDeterminer implements ZhuangDeterminer {

	@Override
	public void determineZhuang(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		FangpaoMajiangPanResult lastPanResult = (FangpaoMajiangPanResult) ju.findLatestFinishedPanResult();
		List<String> playerIds = lastPanResult.allPlayerIds();
		List<String> huPlayerIds = new ArrayList<>();
		for (String playerId : playerIds) {
			if (lastPanResult.ifPlayerHu(playerId)) {
				huPlayerIds.add(playerId);
			}
		}
		if (huPlayerIds.size() > 1) {
			currentPan.setZhuangPlayerId(lastPanResult.getDianpaoPlayerId());
		} else if (huPlayerIds.size() == 1) {
			currentPan.setZhuangPlayerId(huPlayerIds.get(0));
		} else {
			// TODO 最后一个摸牌的人做庄家
		}
	}

}
