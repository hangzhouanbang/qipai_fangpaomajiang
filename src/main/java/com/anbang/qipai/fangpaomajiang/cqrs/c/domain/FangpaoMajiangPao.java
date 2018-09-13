package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

public class FangpaoMajiangPao {
	private int hongzhongShu;
	private int totalscore;// 总得分
	private int value;// 个人结算分

	public FangpaoMajiangPao() {

	}

	public FangpaoMajiangPao(MajiangPlayer player) {
		hongzhongShu = player.countGuipai();
	}

	public void calculate(boolean dapao, boolean sipaofanbei, int playerCount) {
		int pao = 0;
		if (dapao) {
			pao = hongzhongShu;
			if (sipaofanbei) {
				if (pao == 4) {
					pao = 2 * pao;
				}
			}
		}
		value = pao;
		totalscore = pao * (playerCount - 1);
	}

	public int jiesuan(int delta) {
		return totalscore += delta;
	}

	public int getHongzhongShu() {
		return hongzhongShu;
	}

	public void setHongzhongShu(int hongzhongShu) {
		this.hongzhongShu = hongzhongShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}
}
