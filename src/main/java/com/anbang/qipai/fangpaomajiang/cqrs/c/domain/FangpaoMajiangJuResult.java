package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.result.JuResult;

public class FangpaoMajiangJuResult implements JuResult {

	private int finishedPanCount;

	private List<FangpaoMajiangJuPlayerResult> playerResultList;

	private String dayingjiaId;

	private String datuhaoId;

	public int getFinishedPanCount() {
		return finishedPanCount;
	}

	public void setFinishedPanCount(int finishedPanCount) {
		this.finishedPanCount = finishedPanCount;
	}

	public List<FangpaoMajiangJuPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<FangpaoMajiangJuPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

}
