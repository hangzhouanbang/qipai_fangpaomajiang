package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangJuPlayerResult {

	private String playerId;
	private int huCount;
	private int caishenCount;
	private int maxHushu;
	private int gangCount;
	private int paoCount;
	private int niaoCount;
	private int totalScore;

	public void increaseHuCount() {
		huCount++;
	}

	public void increaseCaishenCount(int amount) {
		caishenCount += amount;
	}

	public void tryAndUpdateMaxHushu(int hushu) {
		if (hushu > maxHushu) {
			maxHushu = hushu;
		}
	}

	public void increaseGangCount(int amount) {
		gangCount += amount;
	}

	public void increasePaoCount(int amount) {
		paoCount += amount;
	}

	public void increaseNiaoCount(int amount) {
		niaoCount += amount;
	}

	public void increaseTotalScore(int amount) {
		totalScore += amount;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getHuCount() {
		return huCount;
	}

	public void setHuCount(int huCount) {
		this.huCount = huCount;
	}

	public int getCaishenCount() {
		return caishenCount;
	}

	public void setCaishenCount(int caishenCount) {
		this.caishenCount = caishenCount;
	}

	public int getMaxHushu() {
		return maxHushu;
	}

	public void setMaxHushu(int maxHushu) {
		this.maxHushu = maxHushu;
	}

	public int getGangCount() {
		return gangCount;
	}

	public void setGangCount(int gangCount) {
		this.gangCount = gangCount;
	}

	public int getPaoCount() {
		return paoCount;
	}

	public void setPaoCount(int paoCount) {
		this.paoCount = paoCount;
	}

	public int getNiaoCount() {
		return niaoCount;
	}

	public void setNiaoCount(int niaoCount) {
		this.niaoCount = niaoCount;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
