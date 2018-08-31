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

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
		FangpaoMajiangPanResult fangpaoMajiangPanResult = new FangpaoMajiangPanResult();
		if (huPlayers.size() > 1) {// 一炮多响
			List<String> playerIdList = currentPan.sortedPlayerIdList();
			List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
			// 放炮玩家输给胡家们的胡数
			int delta = 0;
			// 放炮玩家id
			String dianPaoPlayerId = "";
			// 计算胡家胡数
			for (MajiangPlayer huPlayer : huPlayers) {
				FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
				FangpaoMajiangPanPlayerScore huPlayerScore = hu.getScore();
				ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();
				dianPaoPlayerId = hu.getDianpaoPlayerId();
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setScore(huPlayerScore);
				huPlayerScore.jiesuanHuShu(0, 1);
				delta += huPlayerScore.getHushu().jiesuan();
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
			// 计算放炮玩家胡数
			for (String playerId : playerIdList) {
				for (MajiangPlayer huPlayer : huPlayers) {
					if (!playerId.equals(huPlayer.getId())) {
						MajiangPlayer player = currentPan.findPlayerById(playerId);
						FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
						playerResult.setPlayerId(playerId);
						// 计算点炮玩家分数
						playerResult.setScore(FangpaoMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(
								hongzhongcaishen, zhuaniao, niaoshu, currentPan.findPlayerById(playerId)));
						FangpaoMajiangPanPlayerScore buHuPlayerScore = playerResult.getScore();
						buHuPlayerScore.jiesuanHuShu(-delta, 1);

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
					}
				}
			}
			for (FangpaoMajiangPanPlayerResult playerResult : playerResultList) {
				FangpaoMajiangPanPlayerScore score = playerResult.getScore();
				// 结算杠
				FangGangCounter fangGangCounter = ju.getActionStatisticsListenerManager()
						.findListener(FangGangCounter.class);
				Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();
				int fangGangCount = playerFangGangMap.get(playerResult.getPlayerId());
				score.jiesuanGang(playerIdList.size(), fangGangCount);
				// 结算炮
				score.jiesuanPao();
				// 结算鸟
				score.jiesuanNiao();
			}
			playerResultList.forEach((playerResult) -> {
				// 结算最终分数
				playerResult.getScore().calculate();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore().jiesuan());
				} else {
					playerResult.setTotalScore(playerResult.getScore().jiesuan());
				}
			});
			fangpaoMajiangPanResult.setPanNo(currentPan.getNo());
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
			fangpaoMajiangPanResult.setPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(true);
			fangpaoMajiangPanResult.setZimo(false);
			fangpaoMajiangPanResult.setDianpaoPlayerId(dianPaoPlayerId);
			return fangpaoMajiangPanResult;
		}
		if (huPlayers.size() == 1) {// 一人胡
			MajiangPlayer huPlayer = huPlayers.get(0);
			FangpaoMajiangHu hu = (FangpaoMajiangHu) huPlayer.getHu();
			FangpaoMajiangPanPlayerScore huPlayerScore = hu.getScore();
			ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();

			List<String> playerIdList = currentPan.sortedPlayerIdList();
			List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();
			if (hu.isDianpao()) {// 点炮胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setScore(huPlayerScore);
				huPlayerScore.jiesuanHuShu(0, 1);
				// 放炮玩家输给胡家的胡数
				int delta = huPlayerScore.getHushu().jiesuan();
				playerIdList.forEach((playerId) -> {
					FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
					playerResult.setPlayerId(playerId);
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					}
					if (playerId.equals(hu.getDianpaoPlayerId())) {
						// 计算点炮玩家分数
						playerResult.setScore(FangpaoMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(
								hongzhongcaishen, false, niaoshu, currentPan.findPlayerById(playerId)));
						FangpaoMajiangPanPlayerScore buHuPlayerScore = playerResult.getScore();
						buHuPlayerScore.jiesuanHuShu(-delta, 1);
					} else {
						// 计算非胡玩家分数
						playerResult.setScore(FangpaoMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(
								hongzhongcaishen, false, niaoshu, currentPan.findPlayerById(playerId)));
					}
					playerResultList.add(playerResult);
				});
			}
			if (hu.isZimo()) {// 自摸胡
				// 结算胡数
				FangpaoMajiangPanPlayerResult huPlayerResult = new FangpaoMajiangPanPlayerResult();
				huPlayerResult.setPlayerId(huPlayer.getId());
				huPlayerResult.setScore(huPlayerScore);
				huPlayerScore.jiesuanHuShu(0, playerIdList.size());
				// 其他人输给胡家的胡数
				int delta = huPlayerScore.getHushu().jiesuan();
				playerIdList.forEach((playerId) -> {
					FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
					playerResult.setPlayerId(playerId);
					if (playerId.equals(huPlayer.getId())) {
						// 胡家已经计算过了
					} else {
						// 计算非胡玩家分数
						playerResult.setScore(FangpaoMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(
								hongzhongcaishen, false, niaoshu, currentPan.findPlayerById(playerId)));
						FangpaoMajiangPanPlayerScore buHuPlayerScore = playerResult.getScore();
						buHuPlayerScore.jiesuanHuShu(-delta, 0);
					}
					playerResultList.add(playerResult);
				});
			}
			for (FangpaoMajiangPanPlayerResult playerResult : playerResultList) {
				FangpaoMajiangPanPlayerScore score = playerResult.getScore();
				// 结算杠
				FangGangCounter fangGangCounter = ju.getActionStatisticsListenerManager()
						.findListener(FangGangCounter.class);
				Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();
				int fangGangCount = playerFangGangMap.get(playerResult.getPlayerId());
				score.jiesuanGang(playerIdList.size(), fangGangCount);
				// 结算炮
				score.jiesuanPao();
				// 结算鸟
				score.jiesuanNiao();
			}

			// 胡的那家shoupaixing放入结果，其余不胡的shoupailist放入结果
			playerResultList.forEach((playerResult) -> {
				MajiangPlayer player = currentPan.findPlayerById(playerResult.getPlayerId());
				// 结算最终分数
				playerResult.getScore().calculate();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore().jiesuan());
				} else {
					playerResult.setTotalScore(playerResult.getScore().jiesuan());
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

			fangpaoMajiangPanResult.setPanNo(currentPan.getNo());
			fangpaoMajiangPanResult.setPanFinishTime(panFinishTime);
			fangpaoMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
			fangpaoMajiangPanResult.setPlayerResultList(playerResultList);
			fangpaoMajiangPanResult.setHu(true);
			fangpaoMajiangPanResult.setZimo(hu.isZimo());
			fangpaoMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
			return fangpaoMajiangPanResult;
		} else {// 流局
			List<String> playerIdList = currentPan.sortedPlayerIdList();
			List<FangpaoMajiangPanPlayerResult> playerResultList = new ArrayList<>();

			// 结算胡数
			playerIdList.forEach((playerId) -> {
				FangpaoMajiangPanPlayerResult playerResult = new FangpaoMajiangPanPlayerResult();
				playerResult.setPlayerId(playerId);
				// 计算非胡玩家分数
				playerResult.setScore(FangpaoMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(hongzhongcaishen,
						false, niaoshu, currentPan.findPlayerById(playerId)));
				playerResultList.add(playerResult);
			});
			for (FangpaoMajiangPanPlayerResult playerResult : playerResultList) {
				FangpaoMajiangPanPlayerScore score = playerResult.getScore();
				// 结算杠
				FangGangCounter fangGangCounter = ju.getActionStatisticsListenerManager()
						.findListener(FangGangCounter.class);
				Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();
				int fangGangCount = playerFangGangMap.get(playerResult.getPlayerId());
				score.jiesuanGang(playerIdList.size(), fangGangCount);
				// 结算炮
				score.jiesuanPao();
				// 结算鸟
				score.jiesuanNiao();
			}

			// 胡的那家shoupaixing放入结果，其余不胡的shoupailist放入结果
			playerResultList.forEach((playerResult) -> {
				MajiangPlayer player = currentPan.findPlayerById(playerResult.getPlayerId());
				playerResult.getScore().jiesuan();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore().jiesuan());
				} else {
					playerResult.setTotalScore(playerResult.getScore().jiesuan());
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
