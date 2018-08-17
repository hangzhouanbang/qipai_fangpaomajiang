package com.anbang.qipai.fangpaomajiang.cqrs.q.dao;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameState;

public interface MajiangGameDboDao {

	MajiangGameDbo findById(String id);

	void insert(MajiangGameDbo majiangGameDbo);

	void update(String id, byte[] latestPanActionFrameData);

	void update(String id, MajiangGameState state);
}
