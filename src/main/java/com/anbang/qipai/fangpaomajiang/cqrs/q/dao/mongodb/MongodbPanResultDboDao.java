package com.anbang.qipai.fangpaomajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.PanResultDbo;

@Component
public class MongodbPanResultDboDao implements PanResultDboDao {

	@Autowired
	private MongoTemplate mognoTemplate;

	@Override
	public void save(PanResultDbo panResultDbo) {
		mognoTemplate.save(panResultDbo);
	}

	@Override
	public PanResultDbo findByGameIdAndPanNo(String gameId, int panNo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gameId").is(gameId));
		query.addCriteria(Criteria.where("panNo").is(panNo));
		return mognoTemplate.findOne(query, PanResultDbo.class);
	}

}
