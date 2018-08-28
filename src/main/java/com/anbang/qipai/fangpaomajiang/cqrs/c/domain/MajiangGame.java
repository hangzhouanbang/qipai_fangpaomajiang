package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.ju.firstpan.ClassicStartFirstPanProcess;
import com.dml.majiang.ju.nextpan.AllPlayersReadyCreateNextPanDeterminer;
import com.dml.majiang.ju.nextpan.ClassicStartNextPanProcess;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.pan.avaliablepai.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.RandomGuipaiDeterminer;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.player.action.chi.PengganghuFirstChiActionProcessor;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.HuFirstGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.hu.PlayerSetHuHuActionProcessor;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.listener.comprehensive.DianpaoDihuOpportunityDetector;
import com.dml.majiang.player.action.listener.comprehensive.JuezhangStatisticsListener;
import com.dml.majiang.player.action.listener.gang.GangCounter;
import com.dml.majiang.player.action.listener.mo.MoGuipaiCounter;
import com.dml.majiang.player.action.peng.HuFirstPengActionProcessor;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.NoDanpaiOneDuiziGouXingPanHu;
import com.dml.mpgame.game.GamePlayerOnlineState;
import com.dml.mpgame.game.GamePlayerState;
import com.dml.mpgame.game.GamePlayerValueObject;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;

public class MajiangGame {
	private String gameId;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Ju ju;
	private MajiangGameState state;
	private Map<String, MajiangGamePlayerState> playerStateMap = new HashMap<>();
	private Map<String, GamePlayerOnlineState> playerOnlineStateMap = new HashMap<>();
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	public PanActionFrame createJuAndStartFirstPan(GameValueObject game, long currentTime) throws Exception {
		ju = new Ju();
		ju.setStartFirstPanProcess(new ClassicStartFirstPanProcess());
		ju.setStartNextPanProcess(new ClassicStartNextPanProcess());
		ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
		ju.setPlayersMenFengDeterminerForNextPan(new UseFirstPanMenFengPlayersMenFengDeterminer());
		ju.setZhuangDeterminerForFirstPan(new RandomMustHasZhuangZhuangDeterminer(currentTime + 1));
		// TODO 每局由胡牌方做庄家，风位不变；一炮多响时，有点炮者做庄家
		ju.setZhuangDeterminerForNextPan(new DianpaoZhuangDeterminer());

		ju.setAvaliablePaiFiller(new NoHuapaiRandomAvaliablePaiFiller(currentTime + 2));
		ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 3));
		ju.setFaPaiStrategy(new FangpaoMajiangFaPaiStrategy(14));
		// TODO 流局计算
		ju.setCurrentPanFinishiDeterminer(new FangpaoMajiangPanFinishiDeterminer());
		ju.setGouXingPanHu(new NoDanpaiOneDuiziGouXingPanHu());
		ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());
		FangpaoMajiangPanResultBuilder fangpaoMajiangPanResultBuilder = new FangpaoMajiangPanResultBuilder();
		// TODO
		ju.setCurrentPanResultBuilder(fangpaoMajiangPanResultBuilder);
		AllPlayersReadyCreateNextPanDeterminer createNextPanDeterminer = new AllPlayersReadyCreateNextPanDeterminer();
		game.allPlayerIds().forEach((pid) -> createNextPanDeterminer.addPlayer(pid));
		ju.setCreateNextPanDeterminer(createNextPanDeterminer);
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
		ju.setJuResultBuilder(new FangpaoMajiangJuResultBuilder());
		ju.setInitialActionUpdater(new ZhuangMoPaiInitialActionUpdater());
		ju.setMoActionProcessor(new FangpaoMajiangMoActionProcessor());
		ju.setMoActionUpdater(new FangpaoMajiangMoActionUpdater());
		ju.setDaActionProcessor(new DachushoupaiDaActionProcessor());
		ju.setDaActionUpdater(new FangpaoMajiangDaActionUpdater());
		ju.setChiActionProcessor(new PengganghuFirstChiActionProcessor());
		ju.setChiActionUpdater(new FangpaoMajiangChiActionUpdater());
		ju.setPengActionProcessor(new HuFirstPengActionProcessor());
		ju.setPengActionUpdater(new FangpaoMajiangPengActionUpdater());
		ju.setGangActionProcessor(new HuFirstGangActionProcessor());
		ju.setGangActionUpdater(new FangpaoMajiangGangActionUpdater());
		ju.setGuoActionProcessor(new DoNothingGuoActionProcessor());
		ju.setGuoActionUpdater(new FangpaoMajiangGuoActionUpdater());
		ju.setHuActionProcessor(new PlayerSetHuHuActionProcessor());

		ju.addActionStatisticsListener(new CaizipaiListener());
		ju.addActionStatisticsListener(new JuezhangStatisticsListener());
		ju.addActionStatisticsListener(new MoGuipaiCounter());
		ju.addActionStatisticsListener(new GangCounter());
		ju.addActionStatisticsListener(new DianpaoDihuOpportunityDetector());

		// 开始第一盘
		ju.startFirstPan(game.allPlayerIds());

		// 必然庄家已经先摸了一张牌了
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public JuResult finishJu() {
		ju.finish();
		return ju.getJuResult();
	}

	public MajiangGameValueObject updateByGame(GameValueObject game) {

		GameState gameState = game.getState();
		if (gameState.equals(GameState.finished)) {
			state = MajiangGameState.finished;
		} else if (gameState.equals(GameState.playing)) {
			if (state == null || !state.equals(MajiangGameState.waitingNextPan)) {
				state = MajiangGameState.playing;
			}
		} else if (gameState.equals(GameState.waitingStart)) {
			state = MajiangGameState.waitingStart;
		} else {
		}

		List<GamePlayerValueObject> players = game.getPlayers();
		Set<String> playerIdsSet = new HashSet<>();
		players.forEach((player) -> {
			String playerId = player.getId();
			playerIdsSet.add(playerId);
			playerOnlineStateMap.put(playerId, player.getOnlineState());
			GamePlayerState gamePlayerState = player.getState();
			if (gamePlayerState.equals(GamePlayerState.finished)) {
				playerStateMap.put(playerId, MajiangGamePlayerState.finished);
			} else if (gamePlayerState.equals(GamePlayerState.joined)) {
				playerStateMap.put(playerId, MajiangGamePlayerState.joined);
			} else if (gamePlayerState.equals(GamePlayerState.playing)) {
				if (playerStateMap.get(playerId) == null
						|| !playerStateMap.get(playerId).equals(MajiangGamePlayerState.panFinished)) {
					playerStateMap.put(playerId, MajiangGamePlayerState.playing);
				}
			} else if (gamePlayerState.equals(GamePlayerState.readyToStart)) {
				playerStateMap.put(playerId, MajiangGamePlayerState.readyToStart);
			} else {
			}
		});

		Set<String> currentPlayerIdsSet = new HashSet<>(playerStateMap.keySet());
		currentPlayerIdsSet.forEach((playerId) -> {
			if (!playerIdsSet.contains(playerId)) {
				playerStateMap.remove(playerId);
				playerOnlineStateMap.remove(playerId);
				playeTotalScoreMap.remove(playerId);
			}
		});

		return new MajiangGameValueObject(this);

	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getDifen() {
		return difen;
	}

	public void setDifen(int difen) {
		this.difen = difen;
	}

	public int getTaishu() {
		return taishu;
	}

	public void setTaishu(int taishu) {
		this.taishu = taishu;
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

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

	public MajiangGameState getState() {
		return state;
	}

	public void setState(MajiangGameState state) {
		this.state = state;
	}

	public Map<String, MajiangGamePlayerState> getPlayerStateMap() {
		return playerStateMap;
	}

	public void setPlayerStateMap(Map<String, MajiangGamePlayerState> playerStateMap) {
		this.playerStateMap = playerStateMap;
	}

	public Map<String, GamePlayerOnlineState> getPlayerOnlineStateMap() {
		return playerOnlineStateMap;
	}

	public void setPlayerOnlineStateMap(Map<String, GamePlayerOnlineState> playerOnlineStateMap) {
		this.playerOnlineStateMap = playerOnlineStateMap;
	}

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
