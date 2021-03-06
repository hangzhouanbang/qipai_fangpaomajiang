package com.anbang.qipai.fangpaomajiang.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.fangpaomajiang.msg.channel.FangpaoMajiangResultSource;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.fangpaomajiang.msg.msjobj.MajiangHistoricalPanResult;

@EnableBinding(FangpaoMajiangResultSource.class)
public class FangpaoMajiangResultMsgService {

	@Autowired
	private FangpaoMajiangResultSource fangpaoMajiangResultSource;

	public void recordJuResult(MajiangHistoricalJuResult juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("fangpaomajiang ju result");
		mo.setData(juResult);
		fangpaoMajiangResultSource.fangpaoMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}

	public void recordPanResult(MajiangHistoricalPanResult panResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("fangpaomajiang pan result");
		mo.setData(panResult);
		fangpaoMajiangResultSource.fangpaoMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}
}
