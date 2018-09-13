package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.pan.result.PanPlayerResult;

public class FangpaoMajiangPanPlayerResult extends PanPlayerResult {

	private FangpaoMajiangHufen hufen;

	private FangpaoMajiangGang gang;

	private FangpaoMajiangPao pao;

	private FangpaoMajiangNiao niao;

	private int score;// 一盘的结算分

	/**
	 * 可能为负数
	 */
	private int totalScore;

	public FangpaoMajiangHufen getHufen() {
		return hufen;
	}

	public void setHufen(FangpaoMajiangHufen hufen) {
		this.hufen = hufen;
	}

	public FangpaoMajiangGang getGang() {
		return gang;
	}

	public void setGang(FangpaoMajiangGang gang) {
		this.gang = gang;
	}

	public FangpaoMajiangPao getPao() {
		return pao;
	}

	public void setPao(FangpaoMajiangPao pao) {
		this.pao = pao;
	}

	public FangpaoMajiangNiao getNiao() {
		return niao;
	}

	public void setNiao(FangpaoMajiangNiao niao) {
		this.niao = niao;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
