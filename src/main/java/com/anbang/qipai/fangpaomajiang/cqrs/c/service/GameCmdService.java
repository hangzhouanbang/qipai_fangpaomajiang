package com.anbang.qipai.fangpaomajiang.cqrs.c.service;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.dml.mpgame.game.GameValueObject;

public interface GameCmdService {

	MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer panshu, Integer renshu,
			Boolean hongzhongcaishen, Boolean dapao, Boolean sipaofanbei, Boolean zhuaniao, Integer niaoshu);

	MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

	MajiangGameValueObject leaveGame(String playerId) throws Exception;

	MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject finish(String playerId) throws Exception;

	MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

	void bindPlayer(String playerId, String gameId);

	GameValueObject finishGameImmediately(String gameId) throws Exception;

	MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception;

	MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception;
}
