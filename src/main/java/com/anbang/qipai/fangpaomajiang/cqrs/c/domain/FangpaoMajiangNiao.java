package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangNiao {
	private int yiTiaoShu;
	private int yiWanShu;
	private int yiTongShu;
	private int wuTiaoShu;
	private int wuWanShu;
	private int wuTongShu;
	private int jiuTiaoShu;
	private int jiuWanShu;
	private int jiuTongShu;
	private int value;

	public void calculate() {
		int niao = 0;
		niao = yiTiaoShu + yiWanShu + yiTongShu + wuTiaoShu + wuWanShu + wuTongShu + jiuTiaoShu + jiuWanShu
				+ jiuTongShu;
		value = niao;
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
