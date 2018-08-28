package com.anbang.qipai.fangpaomajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameState;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.fangpaomajiang.plan.dao.PlayerInfoDao;
import com.dml.majiang.pan.frame.LiangangangPanActionFramePlayerViewFilter;
import com.dml.majiang.pan.frame.PanActionFrame;

@Component
public class MajiangPlayQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	private LiangangangPanActionFramePlayerViewFilter pvFilter = new LiangangangPanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		MajiangGameDbo majiangGameDbo = majiangGameDboDao.findById(gameId);
		if (!majiangGameDbo.getState().equals(MajiangGameState.playing)) {
			throw new Exception("game not playing");
		}
		byte[] frameData = majiangGameDbo.getLatestPanActionFrameData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		MajiangGameValueObject majiangGame = readyForGameResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (majiangGame.getState().equals(MajiangGameState.playing)) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			majiangGameDboDao.update(majiangGame.getGameId(), panActionFrame.toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		}
	}

	public void readyToNextPan(ReadyToNextPanResult readyToNextPanResult) throws Throwable {

		MajiangGameValueObject majiangGame = readyToNextPanResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			majiangGameDboDao.update(majiangGame.getGameId(),
					readyToNextPanResult.getFirstActionFrame().toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		}
	}

	public JuResultDbo findJuResultDbo(String gameId) {
		return juResultDboDao.findByGameId(gameId);
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) {
		return panResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public void action(MajiangActionResult majiangActionResult) throws Throwable {
		String gameId = majiangActionResult.getMajiangGame().getGameId();
		PanActionFrame panActionFrame = majiangActionResult.getPanActionFrame();
		majiangGameDboDao.update(gameId, panActionFrame.toByteArray(1024 * 8));

		MajiangGameValueObject majiangGame = majiangActionResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		// 盘出结果的话要记录结果
		FangpaoMajiangPanResult fangpaoMajiangPanResult = majiangActionResult.getPanResult();
		if (fangpaoMajiangPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, fangpaoMajiangPanResult);
			panResultDboDao.save(panResultDbo);
			if (majiangActionResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, majiangActionResult.getJuResult());
				juResultDboDao.save(juResultDbo);
			}
		}

		// TODO 记录一条Frame，回放的时候要做
	}
}
