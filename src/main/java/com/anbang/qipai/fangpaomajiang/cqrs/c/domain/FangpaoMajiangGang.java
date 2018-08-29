package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangGang {
	private int pengShu;
	private int fangGangShu;
	private int mingGangShu;
	private int anGangShu;
	private int bieRenAnGangShu;
	private int value;

	public void calculate() {
		int gang = 0;
		gang = pengShu + mingGangShu - fangGangShu * 2 - bieRenAnGangShu * 2;
		value = gang;
	}

	public int jiesuan(int playerCount) {
		return value += anGangShu * playerCount;
	}

	public int getPengShu() {
		return pengShu;
	}

	public void setPengShu(int pengShu) {
		this.pengShu = pengShu;
	}

	public int getFangGangShu() {
		return fangGangShu;
	}

	public void setFangGangShu(int fangGangShu) {
		this.fangGangShu = fangGangShu;
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

	public int getBieRenAnGangShu() {
		return bieRenAnGangShu;
	}

	public void setBieRenAnGangShu(int bieRenAnGangShu) {
		this.bieRenAnGangShu = bieRenAnGangShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
