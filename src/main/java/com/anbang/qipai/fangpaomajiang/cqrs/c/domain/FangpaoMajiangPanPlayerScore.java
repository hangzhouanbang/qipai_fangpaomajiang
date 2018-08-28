package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangPanPlayerScore {

	private FangpaoMajiangPao pao;
	private int paoScore;
	private FangpaoMajiangHushu hushu;

	/**
	 * 有可能是负数
	 */
	private int jiesuanHushu;

	/**
	 * 有可能是负数
	 */
	private int jiesuanPao;

	private int jiesuanScore;

	private int value;

	public void jiesuan() {
		jiesuanScore = jiesuanHushu + jiesuanPao * 100;//TODO 一炮10或者5要配置
	}

	public void jiesuanHushu(int delta) {
		jiesuanHushu += delta;
	}

	public void jiesuanPao(FangpaoMajiangPao anotherPlayerPao) {
		if (pao != null) {
			jiesuanPao += pao.jiesuan(anotherPlayerPao);
		}
	}

	public int quzhengJiesuanScore() {
		return quzheng(jiesuanScore);
	}

	private int quzheng(int value) {
		int shang = value / 10;
		int yu = value % 10;
		if (yu > 0) {
			return (shang + 1) * 10;
		} else {
			return shang * 10;
		}
	}

	public void calculate() {
		if (pao != null) {
			paoScore = pao.getValue() * 10;
		}
		value = hushu.quzhengValue() / 10 + paoScore;
	}

	public FangpaoMajiangPao getPao() {
		return pao;
	}

	public void setPao(FangpaoMajiangPao pao) {
		this.pao = pao;
	}

	public FangpaoMajiangHushu getHushu() {
		return hushu;
	}

	public void setHushu(FangpaoMajiangHushu hushu) {
		this.hushu = hushu;
	}

	public int getPaoScore() {
		return paoScore;
	}

	public void setPaoScore(int paoScore) {
		this.paoScore = paoScore;
	}

	public int getJiesuanHushu() {
		return jiesuanHushu;
	}

	public void setJiesuanHushu(int jiesuanHushu) {
		this.jiesuanHushu = jiesuanHushu;
	}

	public int getJiesuanPao() {
		return jiesuanPao;
	}

	public void setJiesuanPao(int jiesuanPao) {
		this.jiesuanPao = jiesuanPao;
	}

	public int getJiesuanScore() {
		return jiesuanScore;
	}

	public void setJiesuanScore(int jiesuanScore) {
		this.jiesuanScore = jiesuanScore;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
