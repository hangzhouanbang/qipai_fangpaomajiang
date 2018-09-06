package com.anbang.qipai.fangpaomajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGamePlayerState;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameState;
import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.fangpaomajiang.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerOnlineState;

public class MajiangGameDbo {
	private String id;// 就是gameid
	private int panshu;
	private int renshu;
	private boolean hongzhongcaishen;
	private boolean dapao;
	private boolean sipaofanbei;
	private boolean zhuaniao;
	private int niaoshu;
	private MajiangGameState state;
	private List<MajiangGamePlayerDbo> players;
	private byte[] latestPanActionFrameData;// TODO 要按规范拆分重构

	public MajiangGameDbo() {
	}

	public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
		id = majiangGame.getGameId();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		hongzhongcaishen = majiangGame.isHongzhongcaishen();
		dapao = majiangGame.isDapao();
		sipaofanbei = majiangGame.isSipaofanbei();
		zhuaniao = majiangGame.isZhuaniao();
		niaoshu = majiangGame.getNiaoshu();
		state = majiangGame.getState();

		players = new ArrayList<>();
		Map<String, MajiangGamePlayerState> playerStateMap = majiangGame.getPlayerStateMap();
		Map<String, GamePlayerOnlineState> playerOnlineStateMap = majiangGame.getPlayerOnlineStateMap();
		Map<String, Integer> playeTotalScoreMap = majiangGame.getPlayeTotalScoreMap();
		majiangGame.allPlayerIds().forEach((playerId) -> {
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			MajiangGamePlayerDbo playerDbo = new MajiangGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setOnlineState(playerOnlineStateMap.get(playerId));
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerStateMap.get(playerId));
			if (playeTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
			}
			players.add(playerDbo);
		});

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

	public MajiangGameState getState() {
		return state;
	}

	public void setState(MajiangGameState state) {
		this.state = state;
	}

	public List<MajiangGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<MajiangGamePlayerDbo> players) {
		this.players = players;
	}

	public byte[] getLatestPanActionFrameData() {
		return latestPanActionFrameData;
	}

	public void setLatestPanActionFrameData(byte[] latestPanActionFrameData) {
		this.latestPanActionFrameData = latestPanActionFrameData;
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

}
