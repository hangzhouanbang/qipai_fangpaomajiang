package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class FangpaoMajiangHu extends Hu {

	private FangpaoMajiangHufen hufen;

	private boolean huxingHu;// 三财神推倒就不是胡形的胡

	public FangpaoMajiangHu() {
	}

	public FangpaoMajiangHu(ShoupaiPaiXing shoupaiPaiXing, FangpaoMajiangHufen hufen) {
		super(shoupaiPaiXing);
		this.hufen = hufen;
		this.huxingHu = true;
	}

	public FangpaoMajiangHu(FangpaoMajiangHufen hufen) {
		this.hufen = hufen;
		this.huxingHu = false;
	}

	public FangpaoMajiangHufen getHufen() {
		return hufen;
	}

	public void setHufen(FangpaoMajiangHufen hufen) {
		this.hufen = hufen;
	}

	public boolean isHuxingHu() {
		return huxingHu;
	}

	public void setHuxingHu(boolean huxingHu) {
		this.huxingHu = huxingHu;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}
}
