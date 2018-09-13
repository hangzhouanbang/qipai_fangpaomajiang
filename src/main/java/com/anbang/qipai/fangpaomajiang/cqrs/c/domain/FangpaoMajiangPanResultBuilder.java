package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.listener.gang.FangGangCounter;
import com.dml.majiang.player.action.listener.mo.MoGuipaiCounter;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

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
			for (FangpaoMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		FangGangCounter fangGangCounter = ju.getActionStatisticsListenerManager().findListener(FangGangCounter.class);
		Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();
		MoGuipaiCounter moGuipaiCounter = ju.getActionStatisticsListenerManager().findListener(MoGuipaiCounter.class);
		int moGuipai = moGuipaiCounter.getCount();

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
			// 放炮玩家输给胡家们的胡分
			int delta = 0;
			// 计算胡家胡分
			for (MajiangPlayer huPlayer : huPlayers) {
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
				pao.calculate(moGuipai, dapao, sipaofanbei);
				huPlayerResult.setPao(pao);
				// 计算鸟分
				huPlayerResult.setNiao(new FangpaoMajiangNiao());
				// 计算一盘结算分
				int score = huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
						+ huPlayerResult.getPao().getValue() + niao.getValue();
				huPlayerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId()) + score);
				} else {
					huPlayerResult.setTotalScore(score);
				}
				playerResultList.add(huPlayerResult);
			}
			// 计算放炮玩家胡分
			for (String playerId : playerIdList) {
				for (MajiangPlayer huPlayer : huPlayers) {
					if (!playerId.equals(huPlayer.getId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult buHuPlayerResult = new FangpaoMajiangPanPlayerResult();
						buHuPlayerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
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
						pao.calculate(moGuipai, dapao, sipaofanbei);
						buHuPlayerResult.setPao(pao);
						// 计算鸟分
						buHuPlayerResult.setNiao(niao);
						// 计算一盘结算分
						int score = buHuPlayerResult.getHufen().getValue() + buHuPlayerResult.getGang().getValue()
								+ buHuPlayerResult.getPao().getValue() - huPlayers.size() * niao.getValue();
						buHuPlayerResult.setScore(score);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							buHuPlayerResult
									.setTotalScore(playerTotalScoreMap.get(buHuPlayerResult.getPlayerId()) + score);
						} else {
							buHuPlayerResult.setTotalScore(score);
						}
						playerResultList.add(buHuPlayerResult);
					}
				}
			}
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(true);
			fangpaoMajiangPanResult.setZimo(false);
			fangpaoMajiangPanResult.setDianpaoPlayerId(dianPaoPlayerId);
			return fangpaoMajiangPanResult;
		} else if (huPlayers.size() == 1) {// 一人胡
			MajiangPlayer huPlayer = huPlayers.get(0);
			FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
			FangpaoMajiangHufen huPlayerHufen = hu.getHufen();
			ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();
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
				pao.calculate(moGuipai, dapao, sipaofanbei);
				huPlayerResult.setPao(pao);
				// 计算鸟分
				huPlayerResult.setNiao(niao);
				// 计算一盘结算分
				int score = huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
						+ huPlayerResult.getPao().getValue() + niao.getValue();
				huPlayerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId()) + score);
				} else {
					huPlayerResult.setTotalScore(score);
				}
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
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(playerId);
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(buHuplayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						buHuPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(huPlayer);
						pao1.calculate(moGuipai, dapao, sipaofanbei);
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new FangpaoMajiangNiao());
						// 计算一盘结算分
						int score1 = -delta + buHuPlayerResult.getGang().getValue()
								+ buHuPlayerResult.getPao().getValue() - niao.getValue();
						buHuPlayerResult.setScore(score1);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							buHuPlayerResult
									.setTotalScore(playerTotalScoreMap.get(buHuPlayerResult.getPlayerId()) + score1);
						} else {
							buHuPlayerResult.setTotalScore(score1);
						}
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
						pao1.calculate(moGuipai, dapao, sipaofanbei);
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new FangpaoMajiangNiao());
						// 计算一盘结算分
						int score1 = buHuPlayerResult.getHufen().getValue() + buHuPlayerResult.getGang().getValue()
								+ buHuPlayerResult.getPao().getValue() + buHuPlayerResult.getNiao().getValue();
						buHuPlayerResult.setScore(score1);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							buHuPlayerResult
									.setTotalScore(playerTotalScoreMap.get(buHuPlayerResult.getPlayerId()) + score1);
						} else {
							buHuPlayerResult.setTotalScore(score1);
						}
						playerResultList.add(buHuPlayerResult);
					}
				});
			}
			if (hu.isZimo()) {// 自摸胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				// 其他人输给胡家的胡数
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
				pao.calculate(moGuipai, dapao, sipaofanbei);
				huPlayerResult.setPao(pao);
				// 计算鸟分

				huPlayerResult.setNiao(niao);
				// 计算一盘结算分
				int score = (playerIdList.size() - 1) * huPlayerResult.getHufen().getValue()
						+ huPlayerResult.getGang().getValue() + huPlayerResult.getPao().getValue()
						+ (playerIdList.size() - 1) * niao.getValue();
				huPlayerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId()) + score);
				} else {
					huPlayerResult.setTotalScore(score);
				}
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
						pao1.calculate(moGuipai, dapao, sipaofanbei);
						buHuPlayerResult.setPao(pao1);
						// 计算鸟分
						buHuPlayerResult.setNiao(new FangpaoMajiangNiao());
						// 计算一盘结算分
						int score1 = -delta + buHuPlayerResult.getGang().getValue()
								+ buHuPlayerResult.getPao().getValue() - niao.getValue();
						buHuPlayerResult.setScore(score1);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							buHuPlayerResult
									.setTotalScore(playerTotalScoreMap.get(buHuPlayerResult.getPlayerId()) + score1);
						} else {
							buHuPlayerResult.setTotalScore(score1);
						}
						playerResultList.add(buHuPlayerResult);
					}
				}
			}

			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPlayerResultList(playerResultList);
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
				pao.calculate(moGuipai, dapao, sipaofanbei);
				playerResult.setPao(pao);
				// 计算鸟分
				playerResult.setNiao(new FangpaoMajiangNiao());
				// 计算一盘结算分
				int score = playerResult.getHufen().getValue() + playerResult.getGang().getValue()
						+ playerResult.getPao().getValue() + playerResult.getNiao().getValue();
				playerResult.setScore(score);
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + score);
				} else {
					playerResult.setTotalScore(score);
				}
				playerResultList.add(playerResult);
			});

			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setPlayerResultList(playerResultList);
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
