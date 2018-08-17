package com.anbang.qipai.dapaomajiang.plan.dao;

import com.anbang.qipai.dapaomajiang.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);
}
