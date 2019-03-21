package com.anbang.qipai.fangpaomajiang.cqrs.q.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangJuResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.WatchRecordDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.fangpaomajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;

@Component
public class MajiangGameQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private WatchRecordDao watchRecordDao;

	public MajiangGameDbo findMajiangGameDboById(String gameId) {
		return majiangGameDboDao.findById(gameId);
	}

	public void newMajiangGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);
	}

	public void backToGame(String playerId, MajiangGameValueObject majiangGameValueObject) {
		majiangGameDboDao.updatePlayerOnlineState(majiangGameValueObject.getId(), playerId,
				majiangGameValueObject.findPlayerOnlineState(playerId));
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
	}

	public void joinGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);
	}

	public void leaveGame(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGame.getVote();
		if (gameFinishVoteValueObject != null) {
			gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGame.getId());
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
			gameFinishVoteDbo.setGameId(majiangGame.getId());
			gameFinishVoteDboDao.save(gameFinishVoteDbo);
		}
		if (majiangGame.getJuResult() != null) {
			FangpaoMajiangJuResult fangpaoMajiangJuResult = (FangpaoMajiangJuResult) majiangGame.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGame.getId(), null, fangpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void finishGameImmediately(MajiangGameValueObject majiangGameValueObject) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			FangpaoMajiangJuResult fangpaoMajiangJuResult = (FangpaoMajiangJuResult) majiangGameValueObject
					.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, fangpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void finish(MajiangGameValueObject majiangGameValueObject) {
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGameValueObject.getId());
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
			gameFinishVoteDbo.setGameId(majiangGameValueObject.getId());
			gameFinishVoteDboDao.save(gameFinishVoteDbo);
		}
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			FangpaoMajiangJuResult fangpaoMajiangJuResult = (FangpaoMajiangJuResult) majiangGameValueObject
					.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, fangpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void voteToFinish(MajiangGameValueObject majiangGameValueObject) {
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
		}
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			FangpaoMajiangJuResult fangpaoMajiangJuResult = (FangpaoMajiangJuResult) majiangGameValueObject
					.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, fangpaoMajiangJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) {
		return gameFinishVoteDboDao.findByGameId(gameId);
	}

	public WatchRecord saveWatchRecord(String gameId, Watcher watcher) {
		WatchRecord watchRecord = watchRecordDao.findByGameId(gameId);
		if (watchRecord == null) {
			WatchRecord record = new WatchRecord();
			List<Watcher> watchers = new ArrayList<>();
			watchers.add(watcher);

			record.setGameId(gameId);
			record.setWatchers(watchers);
			watchRecordDao.save(record);
			return record;
		}

		watchRecord.getWatchers().add(watcher);
		for (Watcher list : watchRecord.getWatchers()) {
			if (list.getId().equals(watcher.getId())) {
				list.setState(watcher.getState());
				watchRecordDao.save(watchRecord);
				return watchRecord;
			}
		}

		watchRecord.getWatchers().add(watcher);
		watchRecordDao.save(watchRecord);
		return watchRecord;
	}

	public boolean findByPlayerId(String gameId, String playerId) {
		if (watchRecordDao.findByPlayerId(gameId, playerId) != null) {
			return true;
		}
		return false;
	}
}
