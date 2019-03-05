package com.anbang.qipai.fangpaomajiang.cqrs.q.dao;

import com.dml.mpgame.game.watch.WatchRecord;

public interface WatchRecordDao {
    void save(WatchRecord watchRecord);

    WatchRecord findByGameId(String gameId);

    WatchRecord findByPlayerId(String gameId, String playerId);
}
