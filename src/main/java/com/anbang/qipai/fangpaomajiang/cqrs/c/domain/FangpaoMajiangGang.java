package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.player.MajiangPlayer;

public class FangpaoMajiangGang {
	private int mingGangShu;
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
		mingGangShu = 0;
		for (int i = 0; i < yijiupaiArray.length; i++) {
			if (player.ifGangchu(yijiupaiArray[i], GangType.gangdachu)
					|| player.ifGangchu(yijiupaiArray[i], GangType.kezigangmo)) {
				mingGangShu++;
			}
		}
		for (int i = 0; i < erbapaiArray.length; i++) {
			if (player.ifGangchu(yijiupaiArray[i], GangType.gangdachu)
					|| player.ifGangchu(yijiupaiArray[i], GangType.kezigangmo)) {
				mingGangShu++;
			}
		}
		anGangShu = 0;
		for (int i = 0; i < yijiupaiArray.length; i++) {
			if (player.ifGangchu(yijiupaiArray[i], GangType.shoupaigangmo)
					|| player.ifGangchu(yijiupaiArray[i], GangType.gangsigeshoupai)) {
				anGangShu++;
			}
		}

		for (int i = 0; i < erbapaiArray.length; i++) {
			if (player.ifGangchu(erbapaiArray[i], GangType.shoupaigangmo)
					|| player.ifGangchu(erbapaiArray[i], GangType.gangsigeshoupai)) {
				anGangShu++;
			}
		}

	}

	public void calculate(int playerCount, int fangGangCount) {
		value = mingGangShu * 1 + anGangShu * (playerCount - 1) * 2 - fangGangCount * 2;
	}

	public int jiesuan() {
		return value;
	}

	public int getMingGangShu() {
		return mingGangShu;
	}

	public void setMingGangShu(int mingGangShu) {
		this.mingGangShu = mingGangShu;
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
