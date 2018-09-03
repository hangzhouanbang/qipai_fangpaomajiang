package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.ju.result.JuResultBuilder;
import com.dml.majiang.pan.result.PanResult;

public class FangpaoMajiangJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		FangpaoMajiangJuResult fangpaoMajiangJuResult = new FangpaoMajiangJuResult();
		fangpaoMajiangJuResult.setFinishedPanCount(ju.countFinishedPan());
		if (ju.countFinishedPan() > 0) {
			Map<String, FangpaoMajiangJuPlayerResult> juPlayerResultMap = new HashMap<>();
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				FangpaoMajiangPanResult fangpaoMajiangPanResult = (FangpaoMajiangPanResult) panResult;
				for (FangpaoMajiangPanPlayerResult panPlayerResult : fangpaoMajiangPanResult.getPlayerResultList()) {
					FangpaoMajiangJuPlayerResult juPlayerResult = juPlayerResultMap.get(panPlayerResult.getPlayerId());
					if (juPlayerResult == null) {
						juPlayerResult = new FangpaoMajiangJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					if (panPlayerResult.isHu()) {
						juPlayerResult.increaseHuCount();
					}
					juPlayerResult.increaseCaishenCount(panPlayerResult.countCaishen());
					if (panPlayerResult.isZimoHu()) {
						juPlayerResult.increaseZiMoCount();
					}
					if (fangpaoMajiangPanResult.getDianpaoPlayerId().equals(panPlayerResult.getPlayerId())) {
						juPlayerResult.increaseFangPaoCount();
					}
					juPlayerResult.setTotalScore(panPlayerResult.getTotalScore());
				}
			}

			FangpaoMajiangJuPlayerResult dayingjia = null;
			FangpaoMajiangJuPlayerResult datuhao = null;
			for (FangpaoMajiangJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			fangpaoMajiangJuResult.setDatuhaoId(datuhao.getPlayerId());
			fangpaoMajiangJuResult.setDayingjiaId(dayingjia.getPlayerId());
			fangpaoMajiangJuResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return fangpaoMajiangJuResult;
	}

}
