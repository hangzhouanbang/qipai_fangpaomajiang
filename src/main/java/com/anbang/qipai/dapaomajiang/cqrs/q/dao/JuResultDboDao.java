package com.anbang.qipai.dapaomajiang.cqrs.q.dao;

import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboDao {

	void save(JuResultDbo juResultDbo);

	JuResultDbo findByGameId(String gameId);

}
