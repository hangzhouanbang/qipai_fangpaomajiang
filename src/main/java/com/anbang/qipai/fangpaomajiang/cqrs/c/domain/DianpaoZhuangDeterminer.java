package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.action.listener.mo.LastMoActionPlayerRecorder;
import com.dml.majiang.player.zhuang.ZhuangDeterminer;

/**
 * 首局准备后发牌前随机确定坐风和庄，以后每局由胡牌方做庄家，风位不变； 一炮多响时，有点炮者做庄家； 流局后下一局由抓最后一张牌的玩家做庄家。
 * 
 * @author lsc
 *
 */
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
			LastMoActionPlayerRecorder lastMoActionPlayerRecorder = ju.getActionStatisticsListenerManager()
					.findListener(LastMoActionPlayerRecorder.class);
			currentPan.setZhuangPlayerId(lastMoActionPlayerRecorder.getLastMoActionPlayerId());
		}
	}

}
