package com.anbang.qipai.dapaomajiang.cqrs.q.dao;

import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
