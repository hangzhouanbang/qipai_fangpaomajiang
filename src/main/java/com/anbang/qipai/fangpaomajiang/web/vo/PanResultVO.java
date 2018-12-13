package com.anbang.qipai.fangpaomajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.FangpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.PanResultDbo;

public class PanResultVO {

	private List<FangpaoMajiangPanPlayerResultVO> playerResultList;

	private boolean hu;

	private int panNo;

	private long finishTime;

	private List<NiaoPaiVO> niaoPaiList = new ArrayList<>();// 抓到的鸟牌

	private int paiCount;

	private PanActionFrameVO panActionFrame;

	public PanResultVO(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		List<FangpaoMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList
					.add(new FangpaoMajiangPanPlayerResultVO(majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()),
							dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		for (FangpaoMajiangPanPlayerResultVO playerResult : playerResultList) {
			if (playerResult.getNiaoPaiList().size() > 0) {
				setNiaoPaiList(playerResult.getNiaoPaiList());
			}
		}
		hu = dbo.isHu();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
		paiCount = dbo.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().getPaiCount();
		panActionFrame = new PanActionFrameVO(dbo.getPanActionFrame());
	}

	public PanActionFrameVO getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrameVO panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public void setPlayerResultList(List<FangpaoMajiangPanPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public List<FangpaoMajiangPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public boolean isHu() {
		return hu;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public int getPanNo() {
		return panNo;
	}

	public int getPaiCount() {
		return paiCount;
	}

	public void setPaiCount(int paiCount) {
		this.paiCount = paiCount;
	}

	public List<NiaoPaiVO> getNiaoPaiList() {
		return niaoPaiList;
	}

	public void setNiaoPaiList(List<NiaoPaiVO> niaoPaiList) {
		this.niaoPaiList = niaoPaiList;
	}

}
