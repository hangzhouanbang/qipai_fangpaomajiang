package com.anbang.qipai.fangpaomajiang.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.dml.mpgame.game.GamePlayerOnlineState;

@Component
public class MongodbGamePlayerDboDao implements GamePlayerDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(MajiangGamePlayerDbo gamePlayerDbo) {
		mongoTemplate.insert(gamePlayerDbo);
	}

	@Override
	public List<MajiangGamePlayerDbo> findByGameId(String gameId) {
		Query query = new Query(Criteria.where("gameId").is(gameId));
		return mongoTemplate.find(query, MajiangGamePlayerDbo.class);
	}

	@Override
	public MajiangGamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId));
		query.addCriteria(Criteria.where("gameId").is(gameId));
		return mongoTemplate.findOne(query, MajiangGamePlayerDbo.class);
	}

	@Override
	public void update(String playerId, String gameId, MajiangGamePlayerState state) {
		mongoTemplate.updateFirst(new Query(Criteria.where("playerId").is(playerId).and("gameId").is(gameId)),
				new Update().set("state", state), MajiangGamePlayerDbo.class);
	}

	@Override
	public MajiangGamePlayerDbo findNotFinished(String playerId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId));
		query.addCriteria(Criteria.where("state").ne(MajiangGamePlayerState.finished));
		return mongoTemplate.findOne(query, MajiangGamePlayerDbo.class);
	}

	@Override
	public void update(String playerId, String gameId, GamePlayerOnlineState gamePlayerOnlineState) {
		mongoTemplate.updateFirst(new Query(Criteria.where("playerId").is(playerId).and("gameId").is(gameId)),
				new Update().set("onlineState", gamePlayerOnlineState), MajiangGamePlayerDbo.class);
	}

	@Override
	public void removeByPlayerIdAndGameId(String playerId, String gameId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId));
		query.addCriteria(Criteria.where("gameId").is(gameId));
		mongoTemplate.remove(query, MajiangGamePlayerDbo.class);
	}

	@Override
	public void updatePlayersStateForGame(String gameId, MajiangGamePlayerState state) {
		mongoTemplate.updateMulti(new Query(Criteria.where("gameId").is(gameId)), new Update().set("state", state),
				MajiangGamePlayerDbo.class);
	}

	@Override
	public void updateTotalScore(String gameId, String playerId, int totalScore) {
		mongoTemplate.updateFirst(new Query(Criteria.where("playerId").is(playerId).and("gameId").is(gameId)),
				new Update().set("totalScore", totalScore), MajiangGamePlayerDbo.class);
	}

}
