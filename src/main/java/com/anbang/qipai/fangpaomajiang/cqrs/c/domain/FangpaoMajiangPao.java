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

	public void calculate() {
		int pao = 0;
		if (hongzhongShu == 4) {
			pao = 8 * hongzhongShu;
		} else {
			pao = hongzhongShu;
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
