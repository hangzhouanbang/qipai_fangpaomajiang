package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;

public class MajiangActionResult {

	private MajiangGameValueObject majiangGame;
	private PanActionFrame panActionFrame;
	private FangpaoMajiangPanResult panResult;
	private FangpaoMajiangJuResult juResult;

	public MajiangGameValueObject getMajiangGame() {
		return majiangGame;
	}

	public void setMajiangGame(MajiangGameValueObject majiangGame) {
		this.majiangGame = majiangGame;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public FangpaoMajiangPanResult getPanResult() {
		return panResult;
	}

	public void setPanResult(FangpaoMajiangPanResult panResult) {
		this.panResult = panResult;
	}

	public FangpaoMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(FangpaoMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
