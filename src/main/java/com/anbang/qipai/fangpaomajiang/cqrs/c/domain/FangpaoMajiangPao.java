package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangPao {
	private int hongzhongShu;
	private int value;

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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
