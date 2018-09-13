package com.anbang.qipai.fangpaomajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangGang;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangHufen;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangNiao;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.FangpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.ShoupaiDuiziZu;
import com.dml.majiang.player.shoupai.ShoupaiGangziZu;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.player.shoupai.ShoupaiShunziZu;

public class FangpaoMajiangPanPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private boolean zhuang;
	private boolean hu;
	private boolean zimo;
	private boolean dianpao;
	private List<MajiangPai> publicPaiList;
	private List<MajiangPai> caishenList;
	private List<List<ResultShoupaiVO>> resultShoupaiZuList = new ArrayList<>();
	private List<Shunzi> shunziList = new ArrayList<>();
	private List<MajiangPai> keziTypeList = new ArrayList<>();
	private List<GangchuPaiZuVO> gangchuList = new ArrayList<>();

	private int gang;// 非结算杠

	private int pao;// 非结算炮

	private List<NiaoPaiVO> niaoPaiList = new ArrayList<>();// 抓到的鸟牌

	private int niao;// 非结算鸟

	private int hufen;// 非结算胡分

	private FangpaoMajiangHufenVO hufenVo;

	/**
	 * 这个是结算分
	 */
	private int score;

	public FangpaoMajiangPanPlayerResultVO(MajiangGamePlayerDbo gamePlayerDbo, String zhuangPlayerId, boolean zimo,
			String dianpaoPlayerId, FangpaoMajiangPanPlayerResultDbo panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		headimgurl = gamePlayerDbo.getHeadimgurl();
		if (playerId.equals(zhuangPlayerId)) {
			zhuang = true;
		}
		hu = panPlayerResult.getPlayer().getHu() != null;
		publicPaiList = new ArrayList<>(panPlayerResult.getPlayer().getPublicPaiList());
		FangpaoMajiangHufen fangpaoMajiangHufen = panPlayerResult.getPlayerResult().getHufen();
		if (fangpaoMajiangHufen != null) {
			hufenVo = new FangpaoMajiangHufenVO(fangpaoMajiangHufen);
			hufen = fangpaoMajiangHufen.getValue();
		}
		FangpaoMajiangGang fangpaoMajiangGang = panPlayerResult.getPlayerResult().getGang();
		if (fangpaoMajiangGang != null) {
			gang = fangpaoMajiangGang.getValue();
		}
		FangpaoMajiangPao fangpaoMajiangPao = panPlayerResult.getPlayerResult().getPao();
		if (fangpaoMajiangPao != null) {
			pao = fangpaoMajiangPao.getValue();
		}
		FangpaoMajiangNiao fangpaoMajiangNiao = panPlayerResult.getPlayerResult().getNiao();
		if (fangpaoMajiangNiao != null) {
			List<MajiangPai> zhuaPai = fangpaoMajiangNiao.getZhuaPai();
			List<MajiangPai> niaoPai = fangpaoMajiangNiao.getNiaoPai();
			if (zhuaPai != null && niaoPai != null) {
				for (MajiangPai pai : zhuaPai) {
					NiaoPaiVO niaoPaiVo = new NiaoPaiVO();
					niaoPaiVo.setPai(pai);
					if (niaoPai.contains(pai)) {
						niaoPaiVo.setNiaoPai(true);
					}
					niaoPaiList.add(niaoPaiVo);
				}
			}
			niao = fangpaoMajiangNiao.getValue();
		}
		score = panPlayerResult.getPlayerResult().getScore();
		List<ChichuPaiZu> chichuPaiZuList = panPlayerResult.getPlayer().getChichupaiZuList();
		for (ChichuPaiZu chichuPaiZu : chichuPaiZuList) {
			shunziList.add(chichuPaiZu.getShunzi());
		}

		List<PengchuPaiZu> pengchupaiZuList = panPlayerResult.getPlayer().getPengchupaiZuList();
		for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
			keziTypeList.add(pengchuPaiZu.getKezi().getPaiType());
		}

		List<GangchuPaiZu> gangchupaiZuList = panPlayerResult.getPlayer().getGangchupaiZuList();
		for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
			gangchuList.add(new GangchuPaiZuVO(gangchuPaiZu));
		}

		if (hu) {
			this.zimo = zimo;
			ShoupaiPaiXing shoupaiPaiXing = panPlayerResult.getPlayer().getHu().getShoupaiPaiXing();
			List<ShoupaiShunziZu> shunziList = shoupaiPaiXing.getShunziList();
			for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai3()));
			}

			List<ShoupaiKeziZu> keziList = shoupaiPaiXing.getKeziList();
			for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai3()));
			}

			List<ShoupaiGangziZu> gangziList = shoupaiPaiXing.getGangziList();
			for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai3()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai4()));
			}

			List<ShoupaiDuiziZu> duiziList = shoupaiPaiXing.getDuiziList();
			for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai2()));
			}

		} else {
			if (!zimo) {
				if (playerId.equals(dianpaoPlayerId)) {
					dianpao = true;
				}
			}
			List<MajiangPai> shoupaiList = panPlayerResult.getPlayer().getFangruShoupaiList();
			caishenList = new ArrayList<>(panPlayerResult.getPlayer().getFangruGuipaiList());
			List<ResultShoupaiVO> list = new ArrayList<>();
			resultShoupaiZuList.add(list);
			for (MajiangPai pai : shoupaiList) {
				list.add(new ResultShoupaiVO(pai));
			}
		}
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

	public boolean isZhuang() {
		return zhuang;
	}

	public void setZhuang(boolean zhuang) {
		this.zhuang = zhuang;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZimo() {
		return zimo;
	}

	public void setZimo(boolean zimo) {
		this.zimo = zimo;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

	public List<MajiangPai> getPublicPaiList() {
		return publicPaiList;
	}

	public void setPublicPaiList(List<MajiangPai> publicPaiList) {
		this.publicPaiList = publicPaiList;
	}

	public List<MajiangPai> getCaishenList() {
		return caishenList;
	}

	public void setCaishenList(List<MajiangPai> caishenList) {
		this.caishenList = caishenList;
	}

	public List<List<ResultShoupaiVO>> getResultShoupaiZuList() {
		return resultShoupaiZuList;
	}

	public void setResultShoupaiZuList(List<List<ResultShoupaiVO>> resultShoupaiZuList) {
		this.resultShoupaiZuList = resultShoupaiZuList;
	}

	public List<Shunzi> getShunziList() {
		return shunziList;
	}

	public void setShunziList(List<Shunzi> shunziList) {
		this.shunziList = shunziList;
	}

	public List<MajiangPai> getKeziTypeList() {
		return keziTypeList;
	}

	public void setKeziTypeList(List<MajiangPai> keziTypeList) {
		this.keziTypeList = keziTypeList;
	}

	public List<GangchuPaiZuVO> getGangchuList() {
		return gangchuList;
	}

	public void setGangchuList(List<GangchuPaiZuVO> gangchuList) {
		this.gangchuList = gangchuList;
	}

	public int getGang() {
		return gang;
	}

	public void setGang(int gang) {
		this.gang = gang;
	}

	public int getPao() {
		return pao;
	}

	public void setPao(int pao) {
		this.pao = pao;
	}

	public List<NiaoPaiVO> getNiaoPaiList() {
		return niaoPaiList;
	}

	public void setNiaoPaiList(List<NiaoPaiVO> niaoPaiList) {
		this.niaoPaiList = niaoPaiList;
	}

	public int getNiao() {
		return niao;
	}

	public void setNiao(int niao) {
		this.niao = niao;
	}

	public int getHufen() {
		return hufen;
	}

	public void setHufen(int hufen) {
		this.hufen = hufen;
	}

	public FangpaoMajiangHufenVO getHufenVo() {
		return hufenVo;
	}

	public void setHufenVo(FangpaoMajiangHufenVO hufenVo) {
		this.hufenVo = hufenVo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
