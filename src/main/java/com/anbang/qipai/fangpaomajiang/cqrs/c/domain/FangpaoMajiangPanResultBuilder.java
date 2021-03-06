package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.listener.FangpaoMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;

public class FangpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private int niaoshu;
	private boolean zhuaniao;
	private boolean hongzhongcaishen;
	private boolean dapao;
	private boolean sipaofanbei;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		FangpaoMajiangPanResult latestFinishedPanResult = (FangpaoMajiangPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (FangpaoMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		FangpaoMajiangPengGangActionStatisticsListener fangGangCounter = ju.getActionStatisticsListenerManager()
				.findListener(FangpaoMajiangPengGangActionStatisticsListener.class);
		Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
		FangpaoMajiangPanResult fangpaoMajiangPanResult = new FangpaoMajiangPanResult();
		fangpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
		// 鸟分
		FangpaoMajiangNiao niao = new FangpaoMajiangNiao(hongzhongcaishen, zhuaniao, niaoshu);
		niao.calculate();
		// 放炮玩家id
		String dianPaoPlayerId = "";
		if (huPlayers.size() > 1) {// 一炮多响
			Set<String> huPlayerSet = new HashSet<>();
			// 放炮玩家输给胡家们的胡分
			int delta = 0;
			// 计算胡家胡分
			for (MajiangPlayer huPlayer : huPlayers) {
				huPlayerSet.add(huPlayer.getId());
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
				FangpaoMajiangHufen huPlayerHufen = hu.getHufen();
				dianPaoPlayerId = hu.getDianpaoPlayerId();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				delta += huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				FangpaoMajiangGang gang = new FangpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				FangpaoMajiangPao pao = new FangpaoMajiangPao(huPlayer);
				pao.calculate(dapao, sipaofanbei, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				FangpaoMajiangNiao niaofen = new FangpaoMajiangNiao();
				niaofen.jiesuan(-niao.getValue());
				huPlayerResult.setNiao(niaofen);
				playerResultList.add(huPlayerResult);
			}
			// 计算非胡玩家胡分
			for (String playerId : playerIdList) {
				if (!huPlayerSet.contains(playerId)) {
					if (dianPaoPlayerId.equals(playerId)) {// 计算点炮玩家分数
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						FangpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount = playerFangGangMap.get(playerId);
						if (fangGangCount == null) {
							fangGangCount = 0;
						}
						FangpaoMajiangGang gang = new FangpaoMajiangGang(buHuplayer);
						gang.calculate(playerIdList.size(), fangGangCount);
						buHuPlayerResult.setGang(gang);
						// 计算炮分
						FangpaoMajiangPao pao = new FangpaoMajiangPao(buHuplayer);
						pao.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao);
						// 计算鸟分
						niao.jiesuan(niao.getValue() * huPlayers.size());
						buHuPlayerResult.setNiao(niao);
						playerResultList.add(buHuPlayerResult);
					} else {// 计算其他玩家分数
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount = playerFangGangMap.get(playerId);
						if (fangGangCount == null) {
							fangGangCount = 0;
						}
						FangpaoMajiangGang gang = new FangpaoMajiangGang(buHuplayer);
						gang.calculate(playerIdList.size(), fangGangCount);
						buHuPlayerResult.setGang(gang);
						// 计算炮分
						FangpaoMajiangPao pao = new FangpaoMajiangPao(buHuplayer);
						pao.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao);
						// 计算鸟分
						FangpaoMajiangNiao niaofen = new FangpaoMajiangNiao();
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					}
				}
			}
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				FangpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				FangpaoMajiangGang gang1 = playerResult1.getGang();
				FangpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					FangpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					FangpaoMajiangGang gang2 = playerResult2.getGang();
					FangpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			fangpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(true);
			fangpaoMajiangPanResult.setZimo(false);
			fangpaoMajiangPanResult.setDianpaoPlayerId(dianPaoPlayerId);
			return fangpaoMajiangPanResult;
		} else if (huPlayers.size() == 1) {// 一人胡
			MajiangPlayer huPlayer = huPlayers.get(0);
			FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
			FangpaoMajiangHufen huPlayerHufen = hu.getHufen();
			dianPaoPlayerId = hu.getDianpaoPlayerId();
			if (hu.isDianpao()) {// 点炮胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				// 放炮玩家输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				FangpaoMajiangGang gang = new FangpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				FangpaoMajiangPao pao = new FangpaoMajiangPao(huPlayer);
				pao.calculate(dapao, sipaofanbei, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue());
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else if (playerId.equals(hu.getDianpaoPlayerId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						FangpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(playerId);
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						FangpaoMajiangNiao niaofen = new FangpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					} else {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuplayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new FangpaoMajiangNiao());
						playerResultList.add(buHuPlayerResult);
					}
				});
			}
			if (hu.isQianggang()) {// 抢杠胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				// 放炮玩家输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				FangpaoMajiangGang gang = new FangpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				FangpaoMajiangPao pao = new FangpaoMajiangPao(huPlayer);
				pao.calculate(dapao, sipaofanbei, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue());
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else if (playerId.equals(hu.getDianpaoPlayerId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						FangpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(playerId);
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						FangpaoMajiangNiao niaofen = new FangpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					} else {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuplayer));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuplayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(buHuplayer);
						pao1.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new FangpaoMajiangNiao());
						playerResultList.add(buHuPlayerResult);
					}
				});
			}
			if (hu.isZimo()) {// 自摸胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());

				// 其他人输给胡家的胡数
				int delta = huPlayerHufen.getValue();
				huPlayerHufen.setValue(huPlayerHufen.getValue() * (playerIdList.size() - 1));
				huPlayerResult.setHufen(huPlayerHufen);
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				FangpaoMajiangGang gang = new FangpaoMajiangGang(huPlayer);
				gang.calculate(playerIdList.size(), fangGangCount);
				huPlayerResult.setGang(gang);
				// 计算炮分
				FangpaoMajiangPao pao = new FangpaoMajiangPao(huPlayer);
				pao.calculate(dapao, sipaofanbei, playerIdList.size());
				huPlayerResult.setPao(pao);
				// 计算鸟分
				niao.jiesuan(niao.getValue() * (playerIdList.size() - 1));
				huPlayerResult.setNiao(niao);
				playerResultList.add(huPlayerResult);
				for (String playerId : playerIdList) {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else {
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						MajiangPlayer buHuPlayer = currentPan.findPlayerById(playerId);
						buHuPlayerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						buHuPlayerResult
								.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(buHuPlayer));
						FangpaoMajiangHufen hufen = buHuPlayerResult.getHufen();
						hufen.jiesuan(-delta);
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(buHuPlayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuPlayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(buHuPlayer);
						pao1.calculate(dapao, sipaofanbei, playerIdList.size());
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						FangpaoMajiangNiao niaofen = new FangpaoMajiangNiao();
						niaofen.jiesuan(-niao.getValue());
						buHuPlayerResult.setNiao(niaofen);
						playerResultList.add(buHuPlayerResult);
					}
				}
			}
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				FangpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				FangpaoMajiangGang gang1 = playerResult1.getGang();
				FangpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					FangpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					FangpaoMajiangGang gang2 = playerResult2.getGang();
					FangpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			fangpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(true);
			fangpaoMajiangPanResult.setZimo(hu.isZimo());
			fangpaoMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
			return fangpaoMajiangPanResult;
		} else {// 流局
			// 结算胡数
			playerIdList.forEach((playerId) -> {
				MajiangPlayer player = currentPan.findPlayerById(playerId);
				FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
				playerResult.setPlayerId(playerId);
				// 计算非胡玩家分数
				playerResult.setHufen(FangpaoMajiangJiesuanCalculator.calculateBestHuFenForBuhuPlayer(player));
				// 计算杠分
				Integer fangGangCount = playerFangGangMap.get(playerId);
				if (fangGangCount == null) {
					fangGangCount = 0;
				}
				FangpaoMajiangGang gang = new FangpaoMajiangGang(player);
				gang.calculate(playerIdList.size(), fangGangCount);
				playerResult.setGang(gang);
				// 计算炮分
				FangpaoMajiangPao pao = new FangpaoMajiangPao(player);
				pao.calculate(dapao, sipaofanbei, playerIdList.size());
				playerResult.setPao(pao);
				// 计算鸟分
				playerResult.setNiao(new FangpaoMajiangNiao());
				playerResultList.add(playerResult);
			});
			// 两两结算杠、炮
			for (int i = 0; i < playerResultList.size(); i++) {
				FangpaoMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				FangpaoMajiangGang gang1 = playerResult1.getGang();
				FangpaoMajiangPao pao1 = playerResult1.getPao();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					FangpaoMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					FangpaoMajiangGang gang2 = playerResult2.getGang();
					FangpaoMajiangPao pao2 = playerResult2.getPao();
					// 结算杠分
					int zimogang1 = gang1.getZimoMingGangShu();
					int zimogang2 = gang2.getZimoMingGangShu();
					int angang1 = gang1.getAnGangShu();
					int angang2 = gang2.getAnGangShu();
					gang1.jiesuan(-zimogang2 - angang2 * 2);
					gang2.jiesuan(-zimogang1 - angang1 * 2);
					// 结算炮分
					int paovalue1 = pao1.getValue();
					int paovalue2 = pao2.getValue();
					pao1.jiesuan(-paovalue2);
					pao2.jiesuan(-paovalue1);
				}
			}
			playerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				int score = playerResult.getHufen().getValue() + playerResult.getNiao().getTotalScore()
						+ playerResult.getGang().getValue() + playerResult.getPao().getTotalscore();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
			});
			fangpaoMajiangPanResult.setPan(new PanValueObject(currentPan));
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPanPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(false);
			return fangpaoMajiangPanResult;
		}
	}

	public int getNiaoshu() {
		return niaoshu;
	}

	public void setNiaoshu(int niaoshu) {
		this.niaoshu = niaoshu;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public boolean isHongzhongcaishen() {
		return hongzhongcaishen;
	}

	public void setHongzhongcaishen(boolean hongzhongcaishen) {
		this.hongzhongcaishen = hongzhongcaishen;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isSipaofanbei() {
		return sipaofanbei;
	}

	public void setSipaofanbei(boolean sipaofanbei) {
		this.sipaofanbei = sipaofanbei;
	}

}
