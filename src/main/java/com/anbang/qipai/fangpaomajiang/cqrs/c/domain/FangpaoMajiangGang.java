package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangGang {
	private int pengShu;
	private int mingGangShu;
	private int anGangShu;
	private int value;

	public void calculate() {
		int gang = 0;
		gang = pengShu + mingGangShu;
		value = gang;
	}

	public int jiesuan(int playerCount, int fangGangCount) {
		return value += 2 * anGangShu * playerCount - fangGangCount * 2;
	}

	public int getPengShu() {
		return pengShu;
	}

	public void setPengShu(int pengShu) {
		this.pengShu = pengShu;
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
