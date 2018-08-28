package com.anbang.qipai.fangpaomajiang.msg.receiver;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.fangpaomajiang.msg.channel.GameRoomSink;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.CommonMO;
import com.google.gson.Gson;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.FANGPAOGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {

		}
	}
}
