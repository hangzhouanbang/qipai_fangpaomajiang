package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.player.MajiangPlayer;

public class FangpaoMajiangGang {
	private int zimoMingGangShu;
	private int fangGangmingGangShu;
	private int anGangShu;
	private int value;

	public FangpaoMajiangGang() {

	}

	public FangpaoMajiangGang(MajiangPlayer player) {
		MajiangPai[] yijiupaiArray = new MajiangPai[] { MajiangPai.yiwan, MajiangPai.jiuwan, MajiangPai.yitong,
				MajiangPai.jiutong, MajiangPai.yitiao, MajiangPai.jiutiao };
		MajiangPai[] erbapaiArray = new MajiangPai[] { MajiangPai.erwan, MajiangPai.sanwan, MajiangPai.siwan,
				MajiangPai.wuwan, MajiangPai.liuwan, MajiangPai.qiwan, MajiangPai.bawan,

				MajiangPai.ertong, MajiangPai.santong, MajiangPai.sitong, MajiangPai.wutong, MajiangPai.liutong,
				MajiangPai.qitong, MajiangPai.batong,

				MajiangPai.ertiao, MajiangPai.santiao, MajiangPai.sitiao, MajiangPai.wutiao, MajiangPai.liutiao,
				MajiangPai.qitiao, MajiangPai.batiao };
		zimoMingGangShu = 0;
		fangGangmingGangShu = 0;
		anGangShu = 0;
		for (int i = 0; i < yijiupaiArray.length; i++) {
			if (player.ifGangchu(yijiupaiArray[i], GangType.gangdachu)) {
				fangGangmingGangShu++;
			}
			if (player.ifGangchu(yijiupaiArray[i], GangType.kezigangmo)
					|| player.ifGangchu(yijiupaiArray[i], GangType.kezigangshoupai)) {
				zimoMingGangShu++;
			}
			if (player.ifGangchu(yijiupaiArray[i], GangType.shoupaigangmo)
					|| player.ifGangchu(yijiupaiArray[i], GangType.gangsigeshoupai)) {
				anGangShu++;
			}
		}
		for (int i = 0; i < erbapaiArray.length; i++) {
			if (player.ifGangchu(erbapaiArray[i], GangType.gangdachu)) {
				fangGangmingGangShu++;
			}
			if (player.ifGangchu(erbapaiArray[i], GangType.kezigangmo)
					|| player.ifGangchu(erbapaiArray[i], GangType.kezigangshoupai)) {
				zimoMingGangShu++;
			}
			if (player.ifGangchu(erbapaiArray[i], GangType.shoupaigangmo)
					|| player.ifGangchu(erbapaiArray[i], GangType.gangsigeshoupai)) {
				anGangShu++;
			}
		}
	}

	public void calculate(int playerCount, int fangGangCount) {
		// 未扣除别人自摸的分数
		value = (zimoMingGangShu + anGangShu * 2) * (playerCount - 1) - fangGangCount * 2 + fangGangmingGangShu * 2;
	}

	public int jiesuan(int delta) {
		return value += delta;
	}

	public int getZimoMingGangShu() {
		return zimoMingGangShu;
	}

	public void setZimoMingGangShu(int zimoMingGangShu) {
		this.zimoMingGangShu = zimoMingGangShu;
	}

	public int getFangGangmingGangShu() {
		return fangGangmingGangShu;
	}

	public void setFangGangmingGangShu(int fangGangmingGangShu) {
		this.fangGangmingGangShu = fangGangmingGangShu;
	}

	public int getAnGangShu() {
		return anGangShu;
	}

	public void setAnGangShu(int anGangShu) {
		this.anGangShu = anGangShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
