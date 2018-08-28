package com.anbang.qipai.fangpaomajiang.cqrs.q.dao;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameState;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.GamePlayerOnlineState;

public interface MajiangGameDboDao {

	MajiangGameDbo findById(String id);

	void save(MajiangGameDbo majiangGameDbo);

	void update(String id, byte[] latestPanActionFrameData);

	void update(String id, MajiangGameState state);

	void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState);

}
