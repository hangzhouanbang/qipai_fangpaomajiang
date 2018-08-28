package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;
import com.dml.majiang.position.MajiangPosition;

/**
 * 使用第一盘的门风
 * 
 * @author lsc
 *
 */
public class UseFirstPanMenFengPlayersMenFengDeterminer implements PlayersMenFengDeterminer {

	@Override
	public void determinePlayersMenFeng(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
		// 用第一盘的门风给所有玩家设置门风
		List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
		for (String playerId : allPlayerIds) {
			MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
			currentPan.updatePlayerMenFeng(playerId, playerMenFeng);
		}
	}

}
