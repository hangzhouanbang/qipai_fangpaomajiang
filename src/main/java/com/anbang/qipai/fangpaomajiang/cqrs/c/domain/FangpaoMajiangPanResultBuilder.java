package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class FangpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		FangpaoMajiangPanResult latestFinishedPanResult = (FangpaoMajiangPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (FangpaoMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();

		for (MajiangPlayer huPlayer : huPlayers) {// 正常有人胡结束
			FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
			FangpaoMajiangPanPlayerScore huPlayerScore = hu.getScore();
			ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();

			// 两两结算FangpaoMajiangPanPlayerScore
			List<String> playerIdList = currentPan.sortedPlayerIdList();
			List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
			playerIdList.forEach((playerId) -> {
				FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
				playerResult.setPlayerId(playerId);
				if (playerId.equals(huPlayer.getId())) {
					playerResult.setScore(huPlayerScore);
				} else {
					// 计算非胡玩家分数
					playerResult.setScore(FangpaoMajiangJiesuanCalculator
							.calculateBestScoreForBuhuPlayer(currentPan.findPlayerById(playerId)));
				}
				playerResultList.add(playerResult);
			});

			for (int i = 0; i < playerResultList.size(); i++) {

			}

			// 胡的那家shoupaixing放入结果，其余不胡的shoupailist放入结果
			playerResultList.forEach((playerResult) -> {
				MajiangPlayer player = currentPan.findPlayerById(playerResult.getPlayerId());
				playerResult.getScore().jiesuan();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore().getValue());
				} else {
					playerResult.setTotalScore(playerResult.getScore().getValue());
				}
				playerResult.setMenFeng(player.getMenFeng());
				// 吃碰杠出去的要加到结果
				playerResult.setPublicPaiList(new ArrayList<>(player.getPublicPaiList()));
				playerResult.setChichupaiZuList(new ArrayList<>(player.getChichupaiZuList()));
				playerResult.setPengchupaiZuList(new ArrayList<>(player.getPengchupaiZuList()));
				playerResult.setGangchupaiZuList(new ArrayList<>(player.getGangchupaiZuList()));
				playerResult.setGuipaiTypeSet(new HashSet<>(player.getGuipaiTypeSet()));
				playerResult.setShoupaiList(new ArrayList<>(player.getFangruShoupaiList()));
				if (playerResult.getPlayerId().equals(huPlayer.getId())) {
					playerResult.setHu(true);
					playerResult.setBestShoupaiPaiXing(huShoupaiPaiXing);
				} else {
					playerResult.setHu(false);
				}
			});

			FangpaoMajiangPanResult ruianMajiangPanResult = new FangpaoMajiangPanResult();
			ruianMajiangPanResult.setPanNo(currentPan.getNo());
			ruianMajiangPanResult.setPanFinishTime(panFinishTime);
			ruianMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
			ruianMajiangPanResult.setPlayerResultList(playerResultList);
			ruianMajiangPanResult.setHu(true);
			ruianMajiangPanResult.setZimo(hu.isZimo());
			ruianMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
			return ruianMajiangPanResult;
		} // 流局
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
		playerIdList.forEach((playerId) -> {
			FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
			playerResult.setPlayerId(playerId);
			playerResult.setScore(FangpaoMajiangJiesuanCalculator
					.calculateBestScoreForBuhuPlayer(currentPan.findPlayerById(playerId)));
			playerResultList.add(playerResult);
		});

		// shoupailist放入结果
		playerResultList.forEach((playerResult) -> {
			MajiangPlayer player = currentPan.findPlayerById(playerResult.getPlayerId());
			playerResult.getScore().jiesuan();
			// 计算累计总分
			if (latestFinishedPanResult != null) {
				playerResult.setTotalScore(
						playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore().getValue());
			} else {
				playerResult.setTotalScore(playerResult.getScore().getValue());
			}
			playerResult.setMenFeng(player.getMenFeng());
			// 吃碰杠出去的要加到结果
			playerResult.setPublicPaiList(new ArrayList<>(player.getPublicPaiList()));
			playerResult.setChichupaiZuList(new ArrayList<>(player.getChichupaiZuList()));
			playerResult.setPengchupaiZuList(new ArrayList<>(player.getPengchupaiZuList()));
			playerResult.setGangchupaiZuList(new ArrayList<>(player.getGangchupaiZuList()));
			playerResult.setGuipaiTypeSet(new HashSet<>(player.getGuipaiTypeSet()));
			playerResult.setShoupaiList(new ArrayList<>(player.getFangruShoupaiList()));
			playerResult.setHu(false);
		});

		FangpaoMajiangPanResult ruianMajiangPanResult = new FangpaoMajiangPanResult();
		ruianMajiangPanResult.setPanNo(currentPan.getNo());
		ruianMajiangPanResult.setPanFinishTime(panFinishTime);
		ruianMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
		ruianMajiangPanResult.setPlayerResultList(playerResultList);
		ruianMajiangPanResult.setHu(false);

		return ruianMajiangPanResult;
	}

}
