package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

/**
 * 手牌型无关结算参数
 * 
 * @author lsc
 *
 */
public class ShoupaixingWuguanJiesuancanshu {
	private boolean allXushupaiInSameCategory;
	private boolean hasZipai;
	private boolean qingyise;
	private int chichupaiZuCount;

	public ShoupaixingWuguanJiesuancanshu(MajiangPlayer player) {
		allXushupaiInSameCategory = player.allXushupaiInSameCategory();
		hasZipai = player.hasZipai();
		qingyise = (allXushupaiInSameCategory && !hasZipai);
		chichupaiZuCount = player.countChichupaiZu();
	}

	public boolean isAllXushupaiInSameCategory() {
		return allXushupaiInSameCategory;
	}

	public void setAllXushupaiInSameCategory(boolean allXushupaiInSameCategory) {
		this.allXushupaiInSameCategory = allXushupaiInSameCategory;
	}

	public boolean isHasZipai() {
		return hasZipai;
	}

	public void setHasZipai(boolean hasZipai) {
		this.hasZipai = hasZipai;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public void setQingyise(boolean qingyise) {
		this.qingyise = qingyise;
	}

	public int getChichupaiZuCount() {
		return chichupaiZuCount;
	}

	public void setChichupaiZuCount(int chichupaiZuCount) {
		this.chichupaiZuCount = chichupaiZuCount;
	}
}
