package com.anbang.qipai.dapaomajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface FangpaoMajiangGameSource {

	@Output
	MessageChannel fangpaoMajiangGame();
}
