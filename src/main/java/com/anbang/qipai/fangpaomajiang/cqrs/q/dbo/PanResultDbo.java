package com.anbang.qipai.fangpaomajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanPlayerResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanResult;

public class PanResultDbo {
	private String id;
	private String gameId;
	private int panNo;
	private String zhuangPlayerId;
	private boolean hu;
	private boolean zimo;
	private String dianpaoPlayerId;
	private List<FangpaoMajiangPanPlayerResult> playerResultList;
	private long finishTime;

	public PanResultDbo() {
	}

	public PanResultDbo(String gameId, FangpaoMajiangPanResult ruianMajiangPanResult) {
		this.gameId = gameId;
		panNo = ruianMajiangPanResult.getPanNo();
		zhuangPlayerId = ruianMajiangPanResult.getZhuangPlayerId();
		hu = ruianMajiangPanResult.isHu();
		zimo = ruianMajiangPanResult.isZimo();
		dianpaoPlayerId = ruianMajiangPanResult.getDianpaoPlayerId();
		playerResultList = new ArrayList<>(ruianMajiangPanResult.getPlayerResultList());
		finishTime = ruianMajiangPanResult.getPanFinishTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
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

	public String getDianpaoPlayerId() {
		return dianpaoPlayerId;
	}

	public void setDianpaoPlayerId(String dianpaoPlayerId) {
		this.dianpaoPlayerId = dianpaoPlayerId;
	}

	public List<FangpaoMajiangPanPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<FangpaoMajiangPanPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
