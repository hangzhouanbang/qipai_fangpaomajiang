package com.anbang.qipai.dapaomajiang.cqrs.c.domain;

import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

public class VoteToFinishResult {
	private GameFinishVoteValueObject voteValueObject;
	private DianpaoMajiangJuResult juResult;

	public GameFinishVoteValueObject getVoteValueObject() {
		return voteValueObject;
	}

	public void setVoteValueObject(GameFinishVoteValueObject voteValueObject) {
		this.voteValueObject = voteValueObject;
	}

	public DianpaoMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(DianpaoMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
