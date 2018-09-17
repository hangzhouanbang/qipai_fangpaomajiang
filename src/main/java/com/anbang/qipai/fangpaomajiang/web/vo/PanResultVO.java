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

	private int paiCount;

	public PanResultVO(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		List<FangpaoMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList
					.add(new FangpaoMajiangPanPlayerResultVO(majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()),
							dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		hu = dbo.isHu();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
		paiCount = dbo.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().getPaiCount();
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

}
