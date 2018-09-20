package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;

public class MajiangGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {

	private int panshu;
	private int renshu;
	private boolean hongzhongcaishen;
	private boolean dapao;
	private boolean sipaofanbei;
	private boolean zhuaniao;
	private int niaoshu;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	public MajiangGameValueObject(MajiangGame majiangGame) {
		super(majiangGame);
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		hongzhongcaishen = majiangGame.isHongzhongcaishen();
		dapao = majiangGame.isDapao();
		sipaofanbei = majiangGame.isSipaofanbei();
		zhuaniao = majiangGame.isZhuaniao();
		niaoshu = majiangGame.getNiaoshu();
		playeTotalScoreMap.putAll(majiangGame.getPlayeTotalScoreMap());
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isHongzhongcaishen() {
		return hongzhongcaishen;
	}

	public void setHongzhongcaishen(boolean hongzhongcaishen) {
		this.hongzhongcaishen = hongzhongcaishen;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isSipaofanbei() {
		return sipaofanbei;
	}

	public void setSipaofanbei(boolean sipaofanbei) {
		this.sipaofanbei = sipaofanbei;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public int getNiaoshu() {
		return niaoshu;
	}

	public void setNiaoshu(int niaoshu) {
		this.niaoshu = niaoshu;
	}

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
