package com.anbang.qipai.fangpaomajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String FANGPAOGAMEROOM = "fangpaoGameRoom";

	@Input
	SubscribableChannel fangpaoGameRoom();
}
