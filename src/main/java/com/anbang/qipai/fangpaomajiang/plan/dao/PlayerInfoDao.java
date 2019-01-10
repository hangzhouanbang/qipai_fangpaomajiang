package com.anbang.qipai.fangpaomajiang.plan.dao;

import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);

	void updateVip(String playerId, boolean vip);

	void updateMemberBaseInfo(String memberId, String nickname, String headimgurl, String gender);
}
