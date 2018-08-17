package com.anbang.qipai.fangpaomajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.fangpaomajiang.msg.channel.FangpaoMajiangGameSource;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.CommonMO;
import com.dml.mpgame.game.GamePlayer;
import com.dml.mpgame.game.GameValueObject;

@EnableBinding(FangpaoMajiangGameSource.class)
public class FangpaoMajiangGameMsgService {

	@Autowired
	private FangpaoMajiangGameSource fangpaoMajiangGameSource;

	public void gamePlayerLeave(GameValueObject gameValueObject, String playerId) {
		boolean playerIsQuit = true;
		for (GamePlayer gamePlayer : gameValueObject.getPlayers()) {
			if (gamePlayer.getId().equals(playerId)) {
				playerIsQuit = false;
				break;
			}
		}
		if (playerIsQuit) {
			CommonMO mo = new CommonMO();
			mo.setMsg("playerQuit");
			Map data = new HashMap();
			data.put("gameId", gameValueObject.getId());
			data.put("playerId", playerId);
			mo.setData(data);
			fangpaoMajiangGameSource.fangpaoMajiangGame().send(MessageBuilder.withPayload(mo).build());
		}
	}
}
