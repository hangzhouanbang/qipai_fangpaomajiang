package com.anbang.qipai.fangpaomajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerValueObject;
import com.dml.mpgame.game.GameState;

public class MajiangGameDbo {
	private String id;// 就是gameid
	private int panshu;
	private int renshu;
	private boolean hongzhongcaishen;
	private boolean dapao;
	private boolean sipaofanbei;
	private boolean zhuaniao;
	private int niaoshu;
	private int panNo;
	private GameState state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private List<MajiangGamePlayerDbo> players;

	public MajiangGameDbo() {
	}

	public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
		id = majiangGame.getId();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		hongzhongcaishen = majiangGame.isHongzhongcaishen();
		dapao = majiangGame.isDapao();
		sipaofanbei = majiangGame.isSipaofanbei();
		zhuaniao = majiangGame.isZhuaniao();
		niaoshu = majiangGame.getNiaoshu();
		panNo = majiangGame.getPanNo();
		state = majiangGame.getState();

		players = new ArrayList<>();
		Map<String, Integer> playeTotalScoreMap = majiangGame.getPlayeTotalScoreMap();
		for (GamePlayerValueObject playerValueObject : majiangGame.getPlayers()) {
			String playerId = playerValueObject.getId();
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			MajiangGamePlayerDbo playerDbo = new MajiangGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setGender(playerInfo.getGender());
			playerDbo.setOnlineState(playerValueObject.getOnlineState());
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerValueObject.getState());
			if (playeTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
			}
			players.add(playerDbo);
		}

	}

	public MajiangGamePlayerDbo findPlayer(String playerId) {
		for (MajiangGamePlayerDbo player : players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		return null;
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

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public List<MajiangGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<MajiangGamePlayerDbo> players) {
		this.players = players;
	}

}
