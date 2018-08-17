package com.anbang.qipai.dapaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionStatisticsListener;

public class CaizipaiListener
		implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerPengActionStatisticsListener {

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MajiangDaAction daAction, Ju ju) throws Exception {
		// TODO Auto-generated method stub

	}

}
