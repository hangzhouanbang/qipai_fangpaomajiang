package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

public class FinishResult {
	private MajiangGameValueObject majiangGameValueObject;
	private FangpaoMajiangJuResult juResult;

	public MajiangGameValueObject getMajiangGameValueObject() {
		return majiangGameValueObject;
	}

	public void setMajiangGameValueObject(MajiangGameValueObject majiangGameValueObject) {
		this.majiangGameValueObject = majiangGameValueObject;
	}

	public FangpaoMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(FangpaoMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
