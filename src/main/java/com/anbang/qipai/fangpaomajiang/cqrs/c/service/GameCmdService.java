package com.anbang.qipai.fangpaomajiang.cqrs.c.service;

import java.util.Map;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;

public interface GameCmdService {

	MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer panshu, Integer renshu,
			Boolean hongzhongcaishen, Boolean dapao, Boolean sipaofanbei, Boolean zhuaniao, Integer niaoshu);

	MajiangGameValueObject newMajiangGameLeaveAndQuit(String gameId, String playerId, Integer panshu, Integer renshu,
			Boolean hongzhongcaishen, Boolean dapao, Boolean sipaofanbei, Boolean zhuaniao, Integer niaoshu);

	MajiangGameValueObject newMajiangGamePlayerLeaveAndQuit(String gameId, String playerId, Integer panshu,
			Integer renshu, Boolean hongzhongcaishen, Boolean dapao, Boolean sipaofanbei, Boolean zhuaniao,
			Integer niaoshu);

	MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

	MajiangGameValueObject leaveGame(String playerId) throws Exception;

	MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

	MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception;

	void bindPlayer(String playerId, String gameId) throws Exception;

	MajiangGameValueObject finishGameImmediately(String gameId) throws Exception;

	MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception;

	MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception;

	MajiangGameValueObject joinWatch(String playerId, String nickName, String headimgurl, String gameId)
			throws Exception;

	MajiangGameValueObject leaveWatch(String playerId, String gameId) throws Exception;

	Map getwatch(String gameId);

	void recycleWatch(String gameId);
}
