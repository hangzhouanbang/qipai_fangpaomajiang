package com.anbang.qipai.dapaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dapaomajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.JuResultDbo;

@Component
public class MongodbJuResultDboDao implements JuResultDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(JuResultDbo juResultDbo) {
		mongoTemplate.insert(juResultDbo);
	}

	@Override
	public JuResultDbo findByGameId(String gameId) {
		return mongoTemplate.findOne(new Query(Criteria.where("gameId").is(gameId)), JuResultDbo.class);
	}

}
