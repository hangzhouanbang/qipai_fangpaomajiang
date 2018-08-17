package com.anbang.qipai.fangpaomajiang.cqrs.q.dao;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
