package com.anbang.qipai.fangpaomajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.anbang.qipai.fangpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.fpmpv.VotingWhenWaitingNextPan;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;

public class GameVO {
	private String id;// 就是gameid
	private int panshu;
	private int renshu;
	private boolean hongzhongcaishen;
	private boolean dapao;
	private boolean sipaofanbei;
	private boolean zhuaniao;
	private int niaoshu;
	private List<MajiangGamePlayerVO> playerList;
	private Set<String> xipaiPlayerIds;
	private String state;// 原来是 waitingStart, playing, waitingNextPan, finished

	public GameVO(MajiangGameDbo majiangGameDbo) {
		id = majiangGameDbo.getId();
		panshu = majiangGameDbo.getPanshu();
		renshu = majiangGameDbo.getRenshu();
		hongzhongcaishen = majiangGameDbo.isHongzhongcaishen();
		dapao = majiangGameDbo.isDapao();
		sipaofanbei = majiangGameDbo.isSipaofanbei();
		zhuaniao = majiangGameDbo.isZhuaniao();
		niaoshu = majiangGameDbo.getNiaoshu();
		xipaiPlayerIds = majiangGameDbo.getXipaiPlayerIds();
		playerList = new ArrayList<>();
		majiangGameDbo.getPlayers().forEach((dbo) -> playerList.add(new MajiangGamePlayerVO(dbo)));
		String sn = majiangGameDbo.getState().name();
		if (sn.equals(Canceled.name)) {
			state = "canceled";
		} else if (sn.equals(Finished.name)) {
			state = "finished";
		} else if (sn.equals(FinishedByVote.name)) {
			state = "finishedbyvote";
		} else if (sn.equals(Playing.name)) {
			state = "playing";
		} else if (sn.equals(VotingWhenPlaying.name)) {
			state = "playing";
		} else if (sn.equals(VoteNotPassWhenPlaying.name)) {
			state = "playing";
		} else if (sn.equals(VotingWhenWaitingNextPan.name)) {
			state = "waitingNextPan";
		} else if (sn.equals(VoteNotPassWhenWaitingNextPan.name)) {
			state = "waitingNextPan";
		} else if (sn.equals(WaitingNextPan.name)) {
			state = "waitingNextPan";
		} else if (sn.equals(WaitingStart.name)) {
			state = "waitingStart";
		} else {
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isHongzhongcaishen() {
		return hongzhongcaishen;
	}

	public void setHongzhongcaishen(boolean hongzhongcaishen) {
		this.hongzhongcaishen = hongzhongcaishen;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isSipaofanbei() {
		return sipaofanbei;
	}

	public void setSipaofanbei(boolean sipaofanbei) {
		this.sipaofanbei = sipaofanbei;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public int getNiaoshu() {
		return niaoshu;
	}

	public void setNiaoshu(int niaoshu) {
		this.niaoshu = niaoshu;
	}

	public List<MajiangGamePlayerVO> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<MajiangGamePlayerVO> playerList) {
		this.playerList = playerList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<String> getXipaiPlayerIds() {
		return xipaiPlayerIds;
	}

	public void setXipaiPlayerIds(Set<String> xipaiPlayerIds) {
		this.xipaiPlayerIds = xipaiPlayerIds;
	}

}
