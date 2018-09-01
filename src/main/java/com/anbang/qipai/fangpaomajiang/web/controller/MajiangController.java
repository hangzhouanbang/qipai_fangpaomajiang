package com.anbang.qipai.fangpaomajiang.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangNiao;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanPlayerResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangPanPlayerScore;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.fangpaomajiang.cqrs.c.service.MajiangPlayCmdService;
import com.anbang.qipai.fangpaomajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.fangpaomajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.fangpaomajiang.msg.service.FangpaoMajiangGameMsgService;
import com.anbang.qipai.fangpaomajiang.msg.service.FangpaoMajiangResultMsgService;
import com.anbang.qipai.fangpaomajiang.web.vo.CommonVO;
import com.anbang.qipai.fangpaomajiang.web.vo.JuResultVO;
import com.anbang.qipai.fangpaomajiang.web.vo.PanActionFrameVO;
import com.anbang.qipai.fangpaomajiang.web.vo.PanResultVO;
import com.anbang.qipai.fangpaomajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.fangpaomajiang.websocket.QueryScope;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.frame.PanActionFrame;

@RestController
@RequestMapping("/mj")
public class MajiangController {

	@Autowired
	private MajiangPlayCmdService majiangPlayCmdService;

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private PlayerAuthService playerAuthService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private FangpaoMajiangResultMsgService fangpaoMajiangResultMsgService;

	@Autowired
	private FangpaoMajiangGameMsgService gameMsgService;

	/**
	 * 当前盘我应该看到的所有信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/pan_action_frame_for_me")
	@ResponseBody
	public CommonVO panactionframeforme(String token, String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PanActionFrame panActionFrame;
		try {
			panActionFrame = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId, playerId);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		data.put("panActionFrame", new PanActionFrameVO(panActionFrame));
		return vo;
	}

	@RequestMapping(value = "/pan_result")
	@ResponseBody
	public CommonVO panresult(String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, panNo);
		MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
		data.put("panResult", new PanResultVO(panResultDbo, majiangGameDbo));
		return vo;
	}

	/**
	 * 抓鸟时所有玩家都可以访问该接口，当玩家疯狂点击屏幕会占用较多资源
	 */
	@RequestMapping(value = "/zhuaniao")
	@ResponseBody
	public CommonVO zhuaniao(String token, String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, panNo);
		List<String> playerIds = new ArrayList<>();
		String zhuaNiaoPlayerId = "";
		List<FangpaoMajiangPanPlayerResult> playerResultList = panResultDbo.getPlayerResultList();
		for (FangpaoMajiangPanPlayerResult fangpaoMajiangPanPlayerResult : playerResultList) {
			playerIds.add(fangpaoMajiangPanPlayerResult.getPlayerId());
			FangpaoMajiangPanPlayerScore score = fangpaoMajiangPanPlayerResult.getScore();
			FangpaoMajiangNiao niao = score.getNiao();
			List<MajiangPai> zhuaPai = niao.getZhuaPai();
			if (zhuaPai.size() > 0) {
				zhuaNiaoPlayerId = fangpaoMajiangPanPlayerResult.getPlayerId();
			}
		}
		if (!zhuaNiaoPlayerId.equals(playerId)) {
			vo.setSuccess(false);
			vo.setMsg("not zhuaniao player");
			return vo;
		}
		data.put("queryScopes", QueryScope.zhuaNiaoResult);
		for (String otherPlayerId : playerIds) {
			if (!otherPlayerId.equals(zhuaNiaoPlayerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.zhuaNiaoResult.name());
			}
		}
		return vo;
	}

	@RequestMapping(value = "/zhuaniao_result")
	@ResponseBody
	public CommonVO zhuaniaoresult(String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, panNo);
		List<FangpaoMajiangPanPlayerResult> playerResultList = panResultDbo.getPlayerResultList();
		for (FangpaoMajiangPanPlayerResult fangpaoMajiangPanPlayerResult : playerResultList) {
			FangpaoMajiangPanPlayerScore score = fangpaoMajiangPanPlayerResult.getScore();
			FangpaoMajiangNiao niao = score.getNiao();
			List<MajiangPai> zhuaPai = niao.getZhuaPai();
			if (zhuaPai.size() > 0) {
				data.put("zhuaPai", zhuaPai);
			}
		}
		return vo;
	}

	@RequestMapping(value = "/ju_result")
	@ResponseBody
	public CommonVO juresult(String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		data.put("juResult", new JuResultVO(juResultDbo, majiangGameDbo));
		return vo;
	}

	/**
	 * 麻将行牌
	 * 
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/action")
	@ResponseBody
	public CommonVO action(String token, int id) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		MajiangActionResult majiangActionResult;
		try {
			majiangActionResult = majiangPlayCmdService.action(playerId, id, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			majiangPlayQueryService.action(majiangActionResult);
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}

		if (majiangActionResult.getPanResult() == null) {// 盘没结束
			// 通知其他人
			for (String otherPlayerId : majiangActionResult.getMajiangGame().allPlayerIds()) {
				if (!otherPlayerId.equals(playerId)) {
					wsNotifier.notifyToQuery(otherPlayerId, QueryScope.panForMe.name());
				}
			}

			data.put("queryScope", QueryScope.panForMe);

		} else {// 盘结束了

			if (majiangActionResult.getJuResult() != null) {// 局也结束了
				for (String otherPlayerId : majiangActionResult.getMajiangGame().allPlayerIds()) {
					if (!otherPlayerId.equals(playerId)) {
						wsNotifier.notifyToQuery(otherPlayerId, QueryScope.juResult.name());
					}
				}
				MajiangGameDbo majiangGameDbo = majiangGameQueryService
						.findMajiangGameDboById(majiangActionResult.getMajiangGame().getGameId());
				JuResultDbo juResultDbo = majiangPlayQueryService
						.findJuResultDbo(majiangActionResult.getMajiangGame().getGameId());
				JuResultVO juResult = new JuResultVO(juResultDbo, majiangGameDbo);
				fangpaoMajiangResultMsgService.recordJuResult(juResult);

				gameMsgService.gameFinished(majiangActionResult.getMajiangGame().getGameId());
				data.put("queryScope", QueryScope.juResult);
			} else {
				for (String otherPlayerId : majiangActionResult.getMajiangGame().allPlayerIds()) {
					if (!otherPlayerId.equals(playerId)) {
						wsNotifier.notifyToQuery(otherPlayerId, QueryScope.panResult.name());
					}
				}
				data.put("queryScope", QueryScope.panResult);
			}
			gameMsgService.panFinished(majiangActionResult.getMajiangGame(),
					majiangActionResult.getPanActionFrame().getPanAfterAction());

		}

		return vo;
	}

	@RequestMapping(value = "/ready_to_next_pan")
	@ResponseBody
	public CommonVO readytonextpan(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		ReadyToNextPanResult readyToNextPanResult;
		try {
			readyToNextPanResult = majiangPlayCmdService.readyToNextPan(playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			majiangPlayQueryService.readyToNextPan(readyToNextPanResult);
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}

		// 通知其他人
		PanActionFrame firstActionFrame = readyToNextPanResult.getFirstActionFrame();
		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (firstActionFrame != null) {
			queryScopes.add(QueryScope.panForMe);
		}
		for (String otherPlayerId : readyToNextPanResult.getMajiangGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				queryScopes.forEach((scope) -> wsNotifier.notifyToQuery(otherPlayerId, scope.name()));
			}
		}
		data.put("queryScopes", queryScopes);
		return vo;
	}

}
