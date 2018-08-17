package com.anbang.qipai.dapaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dapaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.MajiangGameState;

@Component
public class MongodbMajinagGameDboDao implements MajiangGameDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public MajiangGameDbo findById(String id) {
		return mongoTemplate.findById(id, MajiangGameDbo.class);
	}

	@Override
	public void insert(MajiangGameDbo majiangGameDbo) {
		mongoTemplate.insert(majiangGameDbo);
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

}
