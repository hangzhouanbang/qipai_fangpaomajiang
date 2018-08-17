package com.anbang.qipai.dapaomajiang.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.dapaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.dapaomajiang.plan.dao.PlayerInfoDao;

@Component
public class MongodbPlayerInfoDao implements PlayerInfoDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public PlayerInfo findById(String playerId) {
		return mongoTemplate.findById(playerId, PlayerInfo.class);
	}

	@Override
	public void save(PlayerInfo playerInfo) {
		mongoTemplate.insert(playerInfo);
	}

}
