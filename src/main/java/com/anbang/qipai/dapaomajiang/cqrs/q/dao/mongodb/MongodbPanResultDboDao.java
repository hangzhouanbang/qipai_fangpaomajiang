package com.anbang.qipai.dapaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dapaomajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.dapaomajiang.cqrs.q.dbo.PanResultDbo;

@Component
public class MongodbPanResultDboDao implements PanResultDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(PanResultDbo panResultDbo) {
		mongoTemplate.insert(panResultDbo);
	}

	@Override
	public PanResultDbo findByGameIdAndPanNo(String gameId, int panNo) {
		return mongoTemplate.findOne(
				new Query(Criteria.where("gameId").is(gameId).andOperator(Criteria.where("panNo").is(panNo))),
				PanResultDbo.class);
	}

}
