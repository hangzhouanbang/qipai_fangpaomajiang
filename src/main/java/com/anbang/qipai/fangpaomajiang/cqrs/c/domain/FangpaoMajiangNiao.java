package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangNiao {
	private int yiTiao;
	private int yiWan;
	private int yiTong;
	private int wuTiao;
	private int wuWan;
	private int wuTong;
	private int jiuTiao;
	private int jiuWan;
	private int jiuTong;
	private int value;

	public void calculate() {
		int niao = 0;
		niao = yiTiao + yiWan + yiTong + wuTiao + wuWan + wuTong + jiuTiao + jiuWan + jiuTong;
		value = niao;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
