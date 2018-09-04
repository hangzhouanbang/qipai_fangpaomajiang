package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.GanghouBupai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.GuipaiDangPai;
import com.dml.majiang.player.shoupai.PaiXing;
import com.dml.majiang.player.shoupai.ShoupaiCalculator;
import com.dml.majiang.player.shoupai.ShoupaiGangziZu;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.player.shoupai.ShoupaiWithGuipaiDangGouXingZu;
import com.dml.majiang.player.shoupai.gouxing.GouXing;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class FangpaoMajiangJiesuanCalculator {

	// 自摸胡
	public static FangpaoMajiangHu calculateBestZimoHu(boolean couldTianhu, GouXingPanHu gouXingPanHu,
			MajiangPlayer player, MajiangMoAction moAction) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		if (!player.gangmoGuipai()) {
			shoupaiCalculator.addPai(player.getGangmoShoupai());
		}
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList,
				shoupaiCalculator, player, gouXingPanHu, player.getGangmoShoupai());
		if (!player.gangmoGuipai()) {
			shoupaiCalculator.removePai(player.getGangmoShoupai());
		}

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			FangpaoMajiangHufen bestHuFen = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				FangpaoMajiangHufen hufen = calculateHufen(true, true,
						moAction.getReason().getName().equals(GanghouBupai.name), false, couldTianhu, false,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
				if (bestHuFen == null || bestHuFen.getValue() < hufen.getValue()) {
					bestHuFen = hufen;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new FangpaoMajiangHu(bestHuShoupaiPaiXing, bestHuFen);
		} else {// 不成胡
			return null;
		}
	}

	// 抢杠胡
	public static FangpaoMajiangHu calculateBestQianggangHu(MajiangPai gangPai, GouXingPanHu gouXingPanHu,
			MajiangPlayer player) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做
		shoupaiCalculator.addPai(gangPai);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList,
				shoupaiCalculator, player, gouXingPanHu, player.getGangmoShoupai());
		shoupaiCalculator.removePai(gangPai);
		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			FangpaoMajiangHufen bestHuFen = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				FangpaoMajiangHufen hufen = calculateHufen(true, false, false, true, false, false,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
				if (bestHuFen == null || bestHuFen.getValue() < hufen.getValue()) {
					bestHuFen = hufen;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new FangpaoMajiangHu(bestHuShoupaiPaiXing, bestHuFen);
		} else {// 不成胡
			return null;
		}
	}

	// 点炮胡
	public static FangpaoMajiangHu calculateBestDianpaoHu(boolean couldDihu, GouXingPanHu gouXingPanHu,
			MajiangPlayer player, MajiangPai hupai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList,
				shoupaiCalculator, player, gouXingPanHu, hupai);

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			FangpaoMajiangHufen bestHuFen = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				FangpaoMajiangHufen hufen = calculateHufen(true, false, false, false, false, couldDihu,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
				if (bestHuFen == null || bestHuFen.getValue() < hufen.getValue()) {
					bestHuFen = hufen;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new FangpaoMajiangHu(bestHuShoupaiPaiXing, bestHuFen);
		} else {// 不成胡
			return null;
		}
	}

	public static FangpaoMajiangHufen calculateBestHuFenForBuhuPlayer(MajiangPlayer player) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> shoupaiPaiXingList = calculateBuhuShoupaiPaiXingList(guipaiList, shoupaiCalculator);

		// 要选出分数最高的牌型
		// 先计算和手牌型无关的参数
		ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
		FangpaoMajiangHufen bestHuFen = null;
		for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
			FangpaoMajiangHufen hufen = calculateHufen(false, false, false, false, false, false,
					shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
			if (bestHuFen == null || bestHuFen.getValue() < hufen.getValue()) {
				bestHuFen = hufen;
			}
		}
		return bestHuFen;
	}

	private static FangpaoMajiangHufen calculateHufen(boolean hu, boolean zimoHu, boolean gangkaiHu,
			boolean qianggangHu, boolean couldTianhu, boolean couldDihu,
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu, ShoupaiPaiXing shoupaiPaiXing) {
		FangpaoMajiangHufen hufen = new FangpaoMajiangHufen();
		if (hu) {
			boolean qingyise = shoupaixingWuguanJiesuancanshu.isQingyise();
			boolean qidui = shoupaiPaiXing.getDuiziList().size() == 7;
			boolean pengpenghu = shoupaixingWuguanJiesuancanshu.getChichupaiZuCount() == 0
					&& shoupaiPaiXing.countShunzi() == 0;
			hufen.setHu(hu);// 普通放炮胡
			if (zimoHu) {// 自摸胡
				hufen.setZimoHu(zimoHu);
			}
			if (qianggangHu) {// 抢杠胡
				hufen.setQiangganghu(qianggangHu);
			}
			if (couldTianhu) {// 天胡
				hufen.setTianhu(couldTianhu);
			}
			if (couldDihu) {// 地胡
				hufen.setDihu(couldDihu);
			}
			if (gangkaiHu) {
				hufen.setGangshangkaihua(true);
			}
			if (pengpenghu) {// 碰碰胡
				hufen.setPengpenghu(true);
			}
			if (qidui) {// 七对胡
				hufen.setQiduihu(true);
				if (qingyise) {// 七对清一色
					hufen.setQiduiqingyise(true);
				}
				if (gangkaiHu) {// 七对杠开
					hufen.setQiduigangkai(true);
					;
				}
				// TODO七对单张吊
			}
			if (qingyise) {// 清一色胡
				hufen.setQingyise(true);
				if (pengpenghu) {// 清一色碰碰胡
					hufen.setQingyisepengpenghu(true);
				}
				if (gangkaiHu) {// 清一色杠开
					hufen.setQingyidegangkai(true);
				}
				// TODO清一色单张吊
			}
		}
		hufen.calculate();
		return hufen;
	}

	// 其实点炮,抢杠胡,也包含自摸的意思，也调用这个
	private static List<ShoupaiPaiXing> calculateZimoHuPaiShoupaiPaiXingList(List<MajiangPai> guipaiList,
			ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		if (!guipaiList.isEmpty()) {// 有财神
			return calculateHuPaiShoupaiPaiXingListWithCaishen(guipaiList, shoupaiCalculator, player, gouXingPanHu,
					huPai);
		} else {// 没财神
			return calculateHuPaiShoupaiPaiXingListWithoutCaishen(shoupaiCalculator, player, gouXingPanHu, huPai);
		}
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingList(List<MajiangPai> guipaiList,
			ShoupaiCalculator shoupaiCalculator) {
		if (!guipaiList.isEmpty()) {// 有财神
			return calculateBuhuShoupaiPaiXingListWithCaishen(guipaiList, shoupaiCalculator);
		} else {// 没财神
			return calculateBuhuShoupaiPaiXingListWithoutCaishen(shoupaiCalculator);
		}
	}

	private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithoutCaishen(
			ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算构型
		List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
		int chichuShunziCount = player.countChichupaiZu();
		int pengchuKeziCount = player.countPengchupaiZu();
		int gangchuGangziCount = player.countGangchupaiZu();
		for (GouXing gouXing : gouXingList) {
			boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
					gangchuGangziCount);
			if (hu) {
				// 计算牌型
				List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
				for (PaiXing paiXing : paiXingList) {
					ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
					// 对ShoupaiPaiXing还要变换最后弄进的牌
					List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
							.differentiateShoupaiPaiXingByLastActionPai(huPai);
					huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
				}
			}
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithCaishen(List<MajiangPai> guipaiList,
			ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		int chichuShunziCount = player.countChichupaiZu();
		int pengchuKeziCount = player.countPengchupaiZu();
		int gangchuGangziCount = player.countGangchupaiZu();
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		MajiangPai[] paiTypesForGuipaiAct = calculatePaiTypesForGuipaiAct();// 鬼牌可以扮演的牌类
		// 开始循环财神各种当法，算构型
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = calculateShoupaiWithGuipaiDangGouXingZuList(
				guipaiList, paiTypesForGuipaiAct, shoupaiCalculator);
		// 对于可胡的构型，计算出所有牌型
		for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
			GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
			List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
			for (GouXing gouXing : gouXingList) {
				boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
						gangchuGangziCount);
				if (hu) {
					// 先把所有当的鬼牌加入计算器
					for (int i = 0; i < guipaiDangPaiArray.length; i++) {
						shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
					}
					// 计算牌型
					huPaiShoupaiPaiXingList.addAll(calculateAllShoupaiPaiXingForGouXingWithHupai(gouXing,
							shoupaiCalculator, guipaiDangPaiArray, huPai));
					// 再把所有当的鬼牌移出计算器
					for (int i = 0; i < guipaiDangPaiArray.length; i++) {
						shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
					}
				}

			}
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingListWithoutCaishen(
			ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiPaiXing> buhuShoupaiPaiXingList = new ArrayList<>();
		// 计算构型
		List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
		for (GouXing gouXing : gouXingList) {
			// 计算牌型
			List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
			for (PaiXing paiXing : paiXingList) {
				ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
				buhuShoupaiPaiXingList.add(shoupaiPaiXing);
			}
		}
		return buhuShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingListWithCaishen(List<MajiangPai> guipaiList,
			ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		MajiangPai[] paiTypesForGuipaiAct = calculatePaiTypesForGuipaiAct();// 鬼牌可以扮演的牌类
		// 开始循环财神各种当法，算构型
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = calculateShoupaiWithGuipaiDangGouXingZuList(
				guipaiList, paiTypesForGuipaiAct, shoupaiCalculator);
		// 对构型计算出所有牌型
		for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
			GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
			List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
			for (GouXing gouXing : gouXingList) {
				// 先把所有当的鬼牌加入计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
				}
				// 计算牌型
				huPaiShoupaiPaiXingList.addAll(calculateAllShoupaiPaiXingForGouXingWithoutHupai(gouXing,
						shoupaiCalculator, guipaiDangPaiArray));
				// 再把所有当的鬼牌移出计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
				}
			}
		}
		return huPaiShoupaiPaiXingList;
	}

	/**
	 * 只有序数牌，没有特殊玩法的红中
	 * 
	 * @return
	 */
	private static MajiangPai[] calculatePaiTypesForGuipaiAct() {
		MajiangPai[] xushupaiArray = MajiangPai.xushupaiArray();
		MajiangPai[] paiTypesForGuipaiAct;
		paiTypesForGuipaiAct = new MajiangPai[xushupaiArray.length];
		System.arraycopy(xushupaiArray, 0, paiTypesForGuipaiAct, 0, xushupaiArray.length);
		return paiTypesForGuipaiAct;
	}

	private static List<ShoupaiWithGuipaiDangGouXingZu> calculateShoupaiWithGuipaiDangGouXingZuList(
			List<MajiangPai> guipaiList, MajiangPai[] paiTypesForGuipaiAct, ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = new ArrayList<>();
		int guipaiCount = guipaiList.size();
		int maxZuheCode = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount);
		int[] modArray = new int[guipaiCount];
		for (int i = 0; i < guipaiCount; i++) {
			modArray[i] = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount - 1 - i);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			GuipaiDangPai[] guipaiDangPaiArray = new GuipaiDangPai[guipaiCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int i = 0; i < guipaiCount; i++) {
				int mod = modArray[i];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {
					int yu = temp % mod;
					guipaiDangPaiArray[i] = new GuipaiDangPai(guipaiList.get(i), paiTypesForGuipaiAct[shang]);
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					guipaiDangPaiArray = null;
					break;
				}
			}
			if (guipaiDangPaiArray != null) {
				// 先把所有当的鬼牌加入计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
				}
				// 计算构型
				List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
				// 再把所有当的鬼牌移出计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
				}
				ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu = new ShoupaiWithGuipaiDangGouXingZu();
				shoupaiWithGuipaiDangGouXingZu.setGouXingList(gouXingList);
				shoupaiWithGuipaiDangGouXingZu.setGuipaiDangPaiArray(guipaiDangPaiArray);
				shoupaiWithGuipaiDangGouXingZuList.add(shoupaiWithGuipaiDangGouXingZu);
			}
		}
		return shoupaiWithGuipaiDangGouXingZuList;
	}

	private static List<ShoupaiPaiXing> calculateAllShoupaiPaiXingForGouXingWithHupai(GouXing gouXing,
			ShoupaiCalculator shoupaiCalculator, GuipaiDangPai[] guipaiDangPaiArray, MajiangPai huPai) {
		boolean sancaishen = (guipaiDangPaiArray.length == 3);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByGuipaiDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
			}

			// 对于每一个ShoupaiPaiXing还要变换最后弄进的牌
			for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
				List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
						.differentiateShoupaiPaiXingByLastActionPai(huPai);
				huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
			}

		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateAllShoupaiPaiXingForGouXingWithoutHupai(GouXing gouXing,
			ShoupaiCalculator shoupaiCalculator, GuipaiDangPai[] guipaiDangPaiArray) {
		boolean sancaishen = (guipaiDangPaiArray.length == 3);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByGuipaiDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
			}
			huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingList);
		}
		return huPaiShoupaiPaiXingList;
	}
}
