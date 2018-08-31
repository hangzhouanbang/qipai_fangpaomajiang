package com.anbang.qipai.fangpaomajiang.web.vo;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangJuPlayerResult;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class FangpaoMajiangJuPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private int huCount;
	private int caishenCount;
	private int gangCount;
	private int paoCount;
	private int niaoCount;
	private int maxHushu;
	private int totalScore;

	public FangpaoMajiangJuPlayerResultVO(FangpaoMajiangJuPlayerResult juPlayerResult,
			MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = juPlayerResult.getHuCount();
		caishenCount = juPlayerResult.getCaishenCount();
		gangCount = juPlayerResult.getGangCount();
		paoCount = juPlayerResult.getPaoCount();
		niaoCount = juPlayerResult.getNiaoCount();
		maxHushu = juPlayerResult.getMaxHushu();
		totalScore = juPlayerResult.getTotalScore();
	}

	public FangpaoMajiangJuPlayerResultVO(MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = 0;
		caishenCount = 0;
		gangCount = 0;
		paoCount = 0;
		niaoCount = 0;
		maxHushu = 0;
		totalScore = 0;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
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

	public int getMaxHushu() {
		return maxHushu;
	}

	public void setMaxHushu(int maxHushu) {
		this.maxHushu = maxHushu;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
}
