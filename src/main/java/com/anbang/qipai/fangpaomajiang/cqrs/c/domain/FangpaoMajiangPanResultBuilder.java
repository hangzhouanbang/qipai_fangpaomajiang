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
import com.dml.majiang.player.action.listener.gang.FangGangCounter;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class FangpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private int niaoshu;
	private boolean zhuaniao;
	private boolean hongzhongcaishen;

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

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
		FangpaoMajiangPanResult fangpaoMajiangPanResult = new FangpaoMajiangPanResult();
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
		// 鸟分
		FangpaoMajiangNiao niao = new FangpaoMajiangNiao(hongzhongcaishen, zhuaniao, niaoshu);
		niao.calculate();
		if (huPlayers.size() > 1) {// 一炮多响
			// 放炮玩家输给胡家们的胡分
			int delta = 0;
			// 放炮玩家id
			String dianPaoPlayerId = "";
			// 计算胡家胡分
			for (MajiangPlayer huPlayer : huPlayers) {
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
				FangpaoMajiangHufen huPlayerHufen = hu.getHufen();
				ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();
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
				pao.calculate();
				huPlayerResult.setPao(pao);
				// 计算鸟分
				huPlayerResult.setNiao(new FangpaoMajiangNiao());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId())
							+ huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
							+ huPlayerResult.getPao().getValue() + niao.getValue());
				} else {
					huPlayerResult
							.setTotalScore(huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
									+ huPlayerResult.getPao().getValue() + niao.getValue());
				}
				huPlayerResult.setMenFeng(huPlayer.getMenFeng());
				// 吃碰杠出去的要加到结果
				huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
				huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
				huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
				huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
				huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
				huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
				huPlayerResult.setHu(true);
				huPlayerResult.setBestShoupaiPaiXing(huShoupaiPaiXing);
				playerResultList.add(huPlayerResult);
			}
			// 计算放炮玩家胡分
			for (String playerId : playerIdList) {
				for (MajiangPlayer huPlayer : huPlayers) {
					if (!playerId.equals(huPlayer.getId())) {
						MajiangPlayer buHuplayer = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
						playerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						playerResult.setHufen(FangpaoMajiangJiesuanCalculator
								.calculateBestHuFenForBuhuPlayer(currentPan.findPlayerById(playerId)));
						// 计算杠分
						Integer fangGangCount = playerFangGangMap.get(huPlayer.getId());
						if (fangGangCount == null) {
							fangGangCount = 0;
						}
						FangpaoMajiangGang gang = new FangpaoMajiangGang(huPlayer);
						gang.calculate(playerIdList.size(), fangGangCount);
						playerResult.setGang(gang);
						// 计算炮分
						FangpaoMajiangPao pao = new FangpaoMajiangPao(huPlayer);
						pao.calculate();
						playerResult.setPao(pao);
						// 计算鸟分
						playerResult.setNiao(niao);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) - delta
									+ playerResult.getGang().getValue() + playerResult.getPao().getValue()
									- huPlayers.size() * niao.getValue());
						} else {
							playerResult.setTotalScore(-delta + playerResult.getGang().getValue()
									+ playerResult.getPao().getValue() - huPlayers.size() * niao.getValue());
						}
						playerResult.setMenFeng(buHuplayer.getMenFeng());
						// 吃碰杠出去的要加到结果
						playerResult.setPublicPaiList(new ArrayList<>(buHuplayer.getPublicPaiList()));
						playerResult.setChichupaiZuList(new ArrayList<>(buHuplayer.getChichupaiZuList()));
						playerResult.setPengchupaiZuList(new ArrayList<>(buHuplayer.getPengchupaiZuList()));
						playerResult.setGangchupaiZuList(new ArrayList<>(buHuplayer.getGangchupaiZuList()));
						playerResult.setGuipaiTypeSet(new HashSet<>(buHuplayer.getGuipaiTypeSet()));
						playerResult.setShoupaiList(new ArrayList<>(buHuplayer.getFangruShoupaiList()));
						playerResult.setHu(false);
						playerResultList.add(playerResult);
					}
				}
			}
			fangpaoMajiangPanResult.setPanNo(currentPan.getNo());
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
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
			if (hu.isDianpao()) {// 点炮胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				playerResultList.add(huPlayerResult);
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
				pao.calculate();
				huPlayerResult.setPao(pao);
				// 计算鸟分
				huPlayerResult.setNiao(new FangpaoMajiangNiao());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId())
							+ huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
							+ huPlayerResult.getPao().getValue() + niao.getValue());
				} else {
					huPlayerResult
							.setTotalScore(huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
									+ huPlayerResult.getPao().getValue() + niao.getValue());
				}
				huPlayerResult.setMenFeng(huPlayer.getMenFeng());
				// 吃碰杠出去的要加到结果
				huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
				huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
				huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
				huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
				huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
				huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
				huPlayerResult.setHu(true);
				huPlayerResult.setBestShoupaiPaiXing(huShoupaiPaiXing);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else if (playerId.equals(hu.getDianpaoPlayerId())) {
						FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
						playerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						playerResult.setHufen(FangpaoMajiangJiesuanCalculator
								.calculateBestHuFenForBuhuPlayer(currentPan.findPlayerById(playerId)));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(huPlayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(huPlayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						huPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(huPlayer);
						pao1.calculate();
						huPlayerResult.setPao(pao1);
						// 计算鸟分
						huPlayerResult.setNiao(niao);
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId()) - delta
									+ huPlayerResult.getGang().getValue() + huPlayerResult.getPao().getValue()
									- niao.getValue());
						} else {
							huPlayerResult.setTotalScore(-delta + huPlayerResult.getGang().getValue()
									+ huPlayerResult.getPao().getValue() - niao.getValue());
						}
						huPlayerResult.setMenFeng(huPlayer.getMenFeng());
						// 吃碰杠出去的要加到结果
						huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
						huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
						huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
						huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
						huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
						huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
						huPlayerResult.setHu(false);
						playerResultList.add(huPlayerResult);
					} else {
						FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
						playerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						playerResult.setHufen(FangpaoMajiangJiesuanCalculator
								.calculateBestHuFenForBuhuPlayer(currentPan.findPlayerById(playerId)));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(huPlayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(huPlayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						huPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(huPlayer);
						pao1.calculate();
						huPlayerResult.setPao(pao1);
						// 计算鸟分
						huPlayerResult.setNiao(new FangpaoMajiangNiao());
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId())
									+ huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
									+ huPlayerResult.getPao().getValue() + niao.getValue());
						} else {
							huPlayerResult.setTotalScore(
									huPlayerResult.getHufen().getValue() + huPlayerResult.getGang().getValue()
											+ huPlayerResult.getPao().getValue() + niao.getValue());
						}
						huPlayerResult.setMenFeng(huPlayer.getMenFeng());
						// 吃碰杠出去的要加到结果
						huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
						huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
						huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
						huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
						huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
						huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
						huPlayerResult.setHu(false);
						playerResultList.add(huPlayerResult);
					}
				});
			}
			if (hu.isZimo()) {// 自摸胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setHufen(huPlayerHufen);
				huPlayerResult.setZimoHu(true);
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
				pao.calculate();
				huPlayerResult.setPao(pao);
				// 计算鸟分
				huPlayerResult.setNiao(niao);
				huPlayerResult.setMenFeng(huPlayer.getMenFeng());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId())
							+ (playerIdList.size() - 1) * huPlayerResult.getHufen().getValue()
							+ huPlayerResult.getGang().getValue() + huPlayerResult.getPao().getValue()
							+ (playerIdList.size() - 1) * niao.getValue());
				} else {
					huPlayerResult.setTotalScore((playerIdList.size() - 1) * huPlayerResult.getHufen().getValue()
							+ huPlayerResult.getGang().getValue() + huPlayerResult.getPao().getValue()
							+ (playerIdList.size() - 1) * niao.getValue());
				}
				// 吃碰杠出去的要加到结果
				huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
				huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
				huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
				huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
				huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
				huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
				huPlayerResult.setHu(true);
				huPlayerResult.setBestShoupaiPaiXing(huShoupaiPaiXing);
				playerResultList.add(huPlayerResult);
				playerIdList.forEach((playerId) -> {
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else {
						FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
						playerResult.setPlayerId(playerId);
						// 计算非胡玩家分数
						playerResult.setHufen(FangpaoMajiangJiesuanCalculator
								.calculateBestHuFenForBuhuPlayer(currentPan.findPlayerById(playerId)));
						// 计算杠分
						Integer fangGangCount1 = playerFangGangMap.get(huPlayer.getId());
						if (fangGangCount1 == null) {
							fangGangCount1 = 0;
						}
						FangpaoMajiangGang gang1 = new FangpaoMajiangGang(huPlayer);
						gang1.calculate(playerIdList.size(), fangGangCount1);
						huPlayerResult.setGang(gang1);
						// 计算炮分
						FangpaoMajiangPao pao1 = new FangpaoMajiangPao(huPlayer);
						pao1.calculate();
						huPlayerResult.setPao(pao1);
						// 计算鸟分
						huPlayerResult.setNiao(new FangpaoMajiangNiao());
						// 计算累计总分
						if (latestFinishedPanResult != null) {
							huPlayerResult.setTotalScore(playerTotalScoreMap.get(huPlayerResult.getPlayerId()) - delta
									+ huPlayerResult.getGang().getValue() + huPlayerResult.getPao().getValue()
									- niao.getValue());
						} else {
							huPlayerResult.setTotalScore(-delta + huPlayerResult.getGang().getValue()
									+ huPlayerResult.getPao().getValue() - niao.getValue());
						}
						huPlayerResult.setMenFeng(huPlayer.getMenFeng());
						// 吃碰杠出去的要加到结果
						huPlayerResult.setPublicPaiList(new ArrayList<>(huPlayer.getPublicPaiList()));
						huPlayerResult.setChichupaiZuList(new ArrayList<>(huPlayer.getChichupaiZuList()));
						huPlayerResult.setPengchupaiZuList(new ArrayList<>(huPlayer.getPengchupaiZuList()));
						huPlayerResult.setGangchupaiZuList(new ArrayList<>(huPlayer.getGangchupaiZuList()));
						huPlayerResult.setGuipaiTypeSet(new HashSet<>(huPlayer.getGuipaiTypeSet()));
						huPlayerResult.setShoupaiList(new ArrayList<>(huPlayer.getFangruShoupaiList()));
						huPlayerResult.setHu(false);
						playerResultList.add(huPlayerResult);
					}
				});
			}

			fangpaoMajiangPanResult.setPanNo(currentPan.getNo());
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
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
				playerResult.setHufen(FangpaoMajiangJiesuanCalculator
						.calculateBestHuFenForBuhuPlayer(currentPan.findPlayerById(playerId)));
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
				pao.calculate();
				playerResult.setPao(pao);
				// 计算鸟分
				playerResult.setNiao(new FangpaoMajiangNiao());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId())
							+ playerResult.getHufen().getValue() + playerResult.getGang().getValue()
							+ playerResult.getPao().getValue() + playerResult.getNiao().getValue());
				} else {
					playerResult.setTotalScore(playerResult.getHufen().getValue() + playerResult.getGang().getValue()
							+ playerResult.getPao().getValue() + playerResult.getNiao().getValue());
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
				playerResultList.add(playerResult);
			});

			fangpaoMajiangPanResult.setPanNo(currentPan.getNo());
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
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

}
