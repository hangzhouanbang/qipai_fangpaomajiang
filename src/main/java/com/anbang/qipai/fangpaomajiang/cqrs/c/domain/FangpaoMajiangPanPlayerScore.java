package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FangpaoMajiangPanPlayerScore {
	private FangpaoMajiangHushu hushu;
	private int jiesuanHuShu;// 可能是负数
	private FangpaoMajiangGang gang;
	private int jiesuanGang;// 可能是负数
	private FangpaoMajiangPao pao;
	private int jiesuanPao;
	private FangpaoMajiangNiao niao;
	private int jiesuanNiao;
	private int value;// 可能是负数

	public void jiesuanHuShu(int delta, int playerCount) {
		jiesuanHuShu = hushu.getValue() * playerCount + delta;
	}

	public void jiesuanGang() {
		jiesuanGang = gang.getValue();
	}

	public void jiesuanPao() {
		jiesuanPao = pao.getValue();
	}

	public void jiesuanNiao() {
		jiesuanNiao = niao.getValue();
	}

	public void calculate() {
		value = jiesuanHuShu + jiesuanGang + jiesuanPao + jiesuanNiao;
	}

	public int jiesuan() {
		return value;
	}

	public FangpaoMajiangHushu getHushu() {
		return hushu;
	}

	public void setHushu(FangpaoMajiangHushu hushu) {
		this.hushu = hushu;
	}

	public int getJiesuanHuShu() {
		return jiesuanHuShu;
	}

	public void setJiesuanHuShu(int jiesuanHuShu) {
		this.jiesuanHuShu = jiesuanHuShu;
	}

	public FangpaoMajiangGang getGang() {
		return gang;
	}

	public void setGang(FangpaoMajiangGang gang) {
		this.gang = gang;
	}

	public int getJiesuanGang() {
		return jiesuanGang;
	}

	public void setJiesuanGang(int jiesuanGang) {
		this.jiesuanGang = jiesuanGang;
	}

	public FangpaoMajiangPao getPao() {
		return pao;
	}

	public void setPao(FangpaoMajiangPao pao) {
		this.pao = pao;
	}

	public int getJiesuanPao() {
		return jiesuanPao;
	}

	public void setJiesuanPao(int jiesuanPao) {
		this.jiesuanPao = jiesuanPao;
	}

	public FangpaoMajiangNiao getNiao() {
		return niao;
	}

	public void setNiao(FangpaoMajiangNiao niao) {
		this.niao = niao;
	}

	public int getJiesuanNiao() {
		return jiesuanNiao;
	}

	public void setJiesuanNiao(int jiesuanNiao) {
		this.jiesuanNiao = jiesuanNiao;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
