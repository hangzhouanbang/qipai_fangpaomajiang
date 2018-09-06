package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

public class FangpaoMajiangPao {
	private int hongzhongShu;
	private int value;

	public FangpaoMajiangPao() {

	}

	public FangpaoMajiangPao(MajiangPlayer player) {
		hongzhongShu = player.countGuipai();
	}

	public void calculate(int moGuipai, boolean dapao, boolean sipaofanbei) {
		int pao = 0;
		if (dapao) {
			pao = 2 * hongzhongShu - moGuipai;
			if (sipaofanbei) {
				if (pao == 4) {
					pao = 2 * pao;
				}
				if (pao == -4) {
					pao = 2 * pao;
				}
			}
		}
		value = pao;
	}

	public int jiesuan() {
		return value;
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
}
