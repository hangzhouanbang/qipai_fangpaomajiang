package com.anbang.qipai.fangpaomajiang.cqrs.c.service;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FinishResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;

public interface GameCmdService {

	MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer panshu, Integer renshu,
			Boolean hongzhongcaishen, Boolean zhuaniao, Integer niaoshu);

	MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

	MajiangGameValueObject leaveGame(String playerId) throws Exception;

	MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	FinishResult finish(String playerId) throws Exception;

	FinishResult voteToFinish(String playerId, Boolean yes) throws Exception;

	void bindPlayer(String playerId, String gameId);
}
