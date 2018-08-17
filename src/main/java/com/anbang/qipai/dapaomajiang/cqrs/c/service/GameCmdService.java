package com.anbang.qipai.dapaomajiang.cqrs.c.service;

import com.anbang.qipai.dapaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.dapaomajiang.cqrs.c.domain.VoteToFinishResult;
import com.dml.mpgame.game.GameValueObject;

public interface GameCmdService {

	void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu, Integer renshu,
			Boolean dapao);

	GameValueObject joinGame(String playerId, String gameId) throws Exception;

	void bindPlayer(String playerId, String gameId);

	GameValueObject leaveGame(String playerId) throws Exception;

	GameValueObject backToGame(String playerId, String gameId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	VoteToFinishResult launchFinishVote(String playerId) throws Exception;
}
