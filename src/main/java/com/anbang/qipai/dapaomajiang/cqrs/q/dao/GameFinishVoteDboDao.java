package com.anbang.qipai.dapaomajiang.cqrs.q.dao;

import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

public interface GameFinishVoteDboDao {

	void save(GameFinishVoteDbo gameFinishVoteDbo);

	void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject);

	GameFinishVoteDbo findByGameId(String gameId);

}
