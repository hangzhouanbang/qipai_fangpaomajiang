package com.anbang.qipai.fangpaomajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MemberGoldsSource {
	@Output
	MessageChannel memberGoldsAccounting();
}