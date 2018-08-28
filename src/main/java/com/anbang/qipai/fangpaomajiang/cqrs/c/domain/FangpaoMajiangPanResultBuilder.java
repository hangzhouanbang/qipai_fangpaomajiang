package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;

public class FangpaoMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private int dihu;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDihu() {
		return dihu;
	}

	public void setDihu(int dihu) {
		this.dihu = dihu;
	}
}
