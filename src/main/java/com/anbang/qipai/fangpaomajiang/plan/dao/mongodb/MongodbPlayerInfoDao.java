package com.anbang.qipai.fangpaomajiang.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.fangpaomajiang.plan.dao.PlayerInfoDao;
import com.anbang.qipai.fangpaomajiang.plan.dao.mongodb.repository.PlayerInfoRopository;

@Component
public class MongodbPlayerInfoDao implements PlayerInfoDao {

	@Autowired
	private PlayerInfoRopository repository;

	@Override
	public PlayerInfo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void save(PlayerInfo playerInfo) {
		repository.save(playerInfo);
	}

}
