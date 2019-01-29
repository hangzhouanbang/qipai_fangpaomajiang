package com.anbang.qipai.fangpaomajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.msg.channel.FangpaoMajiangGameSource;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.CommonMO;
import com.dml.majiang.pan.frame.PanValueObject;

@EnableBinding(FangpaoMajiangGameSource.class)
public class FangpaoMajiangGameMsgService {

	@Autowired
	private FangpaoMajiangGameSource fangpaoMajiangGameSource;

	public void gamePlayerLeave(MajiangGameValueObject majiangGameValueObject, String playerId) {
		boolean playerIsQuit = true;
		for (String pid : majiangGameValueObject.allPlayerIds()) {
			if (pid.equals(playerId)) {
				playerIsQuit = false;
				break;
			}
		}
		if (playerIsQuit) {
			CommonMO mo = new CommonMO();
			mo.setMsg("playerQuit");
			Map data = new HashMap();
			data.put("gameId", majiangGameValueObject.getId());
			data.put("playerId", playerId);
			mo.setData(data);
			fangpaoMajiangGameSource.fangpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
		}
	}

	public void gameCanceled(String gameId, String playerId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ju canceled");
		Map data = new HashMap();
		data.put("gameId", gameId);
		data.put("playerId", playerId);
		data.put("leaveTime", System.currentTimeMillis());
		mo.setData(data);
		fangpaoMajiangGameSource.fangpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}

	public void gameFinished(String gameId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ju finished");
		Map data = new HashMap();
		data.put("gameId", gameId);
		mo.setData(data);
		fangpaoMajiangGameSource.fangpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}

	public void panFinished(MajiangGameValueObject majiangGameValueObject, PanValueObject panAfterAction) {
		CommonMO mo = new CommonMO();
		mo.setMsg("pan finished");
		Map data = new HashMap();
		data.put("gameId", majiangGameValueObject.getId());
		data.put("no", panAfterAction.getNo());
		data.put("playerIds", majiangGameValueObject.allPlayerIds());
		mo.setData(data);
		fangpaoMajiangGameSource.fangpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}
}
