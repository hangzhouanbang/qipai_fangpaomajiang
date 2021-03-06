package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.pan.result.PanResult;

public class FangpaoMajiangPanResult extends PanResult {

	private boolean hu;

	private boolean zimo;

	private String dianpaoPlayerId;

	private List<FangpaoMajiangPanPlayerResult> panPlayerResultList;

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

	public List<FangpaoMajiangPanPlayerResult> getPanPlayerResultList() {
		return panPlayerResultList;
	}

	public void setPanPlayerResultList(List<FangpaoMajiangPanPlayerResult> panPlayerResultList) {
		this.panPlayerResultList = panPlayerResultList;
	}

}
