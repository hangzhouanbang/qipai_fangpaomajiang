package com.anbang.qipai.fangpaomajiang.plan.dao;

import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);
}
