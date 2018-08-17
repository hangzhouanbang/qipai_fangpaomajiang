package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

public class VoteToFinishResult {
	private GameFinishVoteValueObject voteValueObject;
	private FangpaoMajiangJuResult juResult;

	public GameFinishVoteValueObject getVoteValueObject() {
		return voteValueObject;
	}

	public void setVoteValueObject(GameFinishVoteValueObject voteValueObject) {
		this.voteValueObject = voteValueObject;
	}

	public FangpaoMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(FangpaoMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
