package com.anbang.qipai.fangpaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameState;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.GamePlayerOnlineState;

@Component
public class MongodbMajiangGameDboDao implements MajiangGameDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public MajiangGameDbo findById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, MajiangGameDbo.class);
	}

	@Override
	public void save(MajiangGameDbo majiangGameDbo) {
		mongoTemplate.save(majiangGameDbo);
	}

	@Override
	public void update(String id, byte[] latestPanActionFrameData) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
				new Update().set("latestPanActionFrameData", latestPanActionFrameData), MajiangGameDbo.class);
	}

	@Override
	public void update(String id, MajiangGameState state) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("state", state),
				MajiangGameDbo.class);
	}

	@Override
	public void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState) {
		Query query = new Query(Criteria.where("id").is(id));
		MajiangGameDbo majiangGameDbo = mongoTemplate.findOne(query, MajiangGameDbo.class);
		majiangGameDbo.getPlayers().forEach((player) -> {
			if (player.getPlayerId().equals(playerId)) {
				player.setOnlineState(onlineState);
			}
		});
		mongoTemplate.save(majiangGameDbo);
	}

}
