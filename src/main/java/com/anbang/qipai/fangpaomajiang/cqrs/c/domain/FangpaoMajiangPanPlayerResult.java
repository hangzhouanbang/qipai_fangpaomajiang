package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.position.MajiangPosition;

public class FangpaoMajiangPanPlayerResult {

	private String playerId;

	private MajiangPosition menFeng;

	private boolean hu;

	private boolean zimoHu;

	private FangpaoMajiangHufen hufen;

	private FangpaoMajiangGang gang;

	private FangpaoMajiangPao pao;

	private FangpaoMajiangNiao niao;

	/**
	 * 可能为负数
	 */
	private int totalScore;

	private ShoupaiPaiXing bestShoupaiPaiXing;

	/**
	 * 手牌列表（包含鬼牌和刚摸的，不包含公开牌）
	 */
	private List<MajiangPai> shoupaiList;

	/**
	 * 公开的牌，不能行牌
	 */
	private List<MajiangPai> publicPaiList;

	/**
	 * 标示什么牌是鬼牌
	 */
	private Set<MajiangPai> guipaiTypeSet;

	private List<ChichuPaiZu> chichupaiZuList;
	private List<PengchuPaiZu> pengchupaiZuList;
	private List<GangchuPaiZu> gangchupaiZuList;

	public int countCaishen() {
		int count = 0;
		for (MajiangPai pai : shoupaiList) {
			if (guipaiTypeSet.contains(pai)) {
				count++;
			}
		}
		return count;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public MajiangPosition getMenFeng() {
		return menFeng;
	}

	public void setMenFeng(MajiangPosition menFeng) {
		this.menFeng = menFeng;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
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

	public ShoupaiPaiXing getBestShoupaiPaiXing() {
		return bestShoupaiPaiXing;
	}

	public void setBestShoupaiPaiXing(ShoupaiPaiXing bestShoupaiPaiXing) {
		this.bestShoupaiPaiXing = bestShoupaiPaiXing;
	}

	public List<MajiangPai> getShoupaiList() {
		return shoupaiList;
	}

	public void setShoupaiList(List<MajiangPai> shoupaiList) {
		this.shoupaiList = shoupaiList;
	}

	public List<MajiangPai> getPublicPaiList() {
		return publicPaiList;
	}

	public void setPublicPaiList(List<MajiangPai> publicPaiList) {
		this.publicPaiList = publicPaiList;
	}

	public Set<MajiangPai> getGuipaiTypeSet() {
		return guipaiTypeSet;
	}

	public void setGuipaiTypeSet(Set<MajiangPai> guipaiTypeSet) {
		this.guipaiTypeSet = guipaiTypeSet;
	}

	public List<ChichuPaiZu> getChichupaiZuList() {
		return chichupaiZuList;
	}

	public void setChichupaiZuList(List<ChichuPaiZu> chichupaiZuList) {
		this.chichupaiZuList = chichupaiZuList;
	}

	public List<PengchuPaiZu> getPengchupaiZuList() {
		return pengchupaiZuList;
	}

	public void setPengchupaiZuList(List<PengchuPaiZu> pengchupaiZuList) {
		this.pengchupaiZuList = pengchupaiZuList;
	}

	public List<GangchuPaiZu> getGangchupaiZuList() {
		return gangchupaiZuList;
	}

	public void setGangchupaiZuList(List<GangchuPaiZu> gangchupaiZuList) {
		this.gangchupaiZuList = gangchupaiZuList;
	}

	public FangpaoMajiangHufen getHufen() {
		return hufen;
	}

	public void setHufen(FangpaoMajiangHufen hufen) {
		this.hufen = hufen;
	}

	public boolean isZimoHu() {
		return zimoHu;
	}

	public void setZimoHu(boolean zimoHu) {
		this.zimoHu = zimoHu;
	}

}
