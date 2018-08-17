package com.anbang.qipai.dapaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dapaomajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

@Component
public class MongodbGameFinishVoteDboDao implements GameFinishVoteDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(GameFinishVoteDbo gameFinishVoteDbo) {
		mongoTemplate.insert(gameFinishVoteDbo);
	}

	@Override
	public void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject) {
		mongoTemplate.updateFirst(new Query(Criteria.where("gameId").is(gameId)),
				new Update().set("vote", gameFinishVoteValueObject), GameFinishVoteDbo.class);
	}

	@Override
	public GameFinishVoteDbo findByGameId(String gameId) {
		return mongoTemplate.findOne(new Query(Criteria.where("gameId").is(gameId)), GameFinishVoteDbo.class);
	}

}
