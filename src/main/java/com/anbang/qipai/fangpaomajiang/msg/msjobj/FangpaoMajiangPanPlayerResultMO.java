package com.anbang.qipai.fangpaomajiang.msg.msjobj;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.FangpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class FangpaoMajiangPanPlayerResultMO {
	private String id;// 玩家id
	private String nickname;// 玩家昵称
	private int score;// 一盘总分

	public FangpaoMajiangPanPlayerResultMO(MajiangGamePlayerDbo gamePlayerDbo,
			FangpaoMajiangPanPlayerResultDbo panPlayerResult) {
		id = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		score = panPlayerResult.getPlayerResult().getScore();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
