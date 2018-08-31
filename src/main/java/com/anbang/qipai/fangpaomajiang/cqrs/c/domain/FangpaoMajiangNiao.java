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

	public int getYiTiaoShu() {
		return yiTiaoShu;
	}

	public void setYiTiaoShu(int yiTiaoShu) {
		this.yiTiaoShu = yiTiaoShu;
	}

	public int getYiWanShu() {
		return yiWanShu;
	}

	public void setYiWanShu(int yiWanShu) {
		this.yiWanShu = yiWanShu;
	}

	public int getYiTongShu() {
		return yiTongShu;
	}

	public void setYiTongShu(int yiTongShu) {
		this.yiTongShu = yiTongShu;
	}

	public int getWuTiaoShu() {
		return wuTiaoShu;
	}

	public void setWuTiaoShu(int wuTiaoShu) {
		this.wuTiaoShu = wuTiaoShu;
	}

	public int getWuWanShu() {
		return wuWanShu;
	}

	public void setWuWanShu(int wuWanShu) {
		this.wuWanShu = wuWanShu;
	}

	public int getWuTongShu() {
		return wuTongShu;
	}

	public void setWuTongShu(int wuTongShu) {
		this.wuTongShu = wuTongShu;
	}

	public int getJiuTiaoShu() {
		return jiuTiaoShu;
	}

	public void setJiuTiaoShu(int jiuTiaoShu) {
		this.jiuTiaoShu = jiuTiaoShu;
	}

	public int getJiuWanShu() {
		return jiuWanShu;
	}

	public void setJiuWanShu(int jiuWanShu) {
		this.jiuWanShu = jiuWanShu;
	}

	public int getJiuTongShu() {
		return jiuTongShu;
	}

	public void setJiuTongShu(int jiuTongShu) {
		this.jiuTongShu = jiuTongShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
