package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;

public class FangpaoMajiangNiao {
	private List<MajiangPai> zhuaPai;
	private List<MajiangPai> niaoPai;
	private int value;

	public FangpaoMajiangNiao() {
		zhuaPai = new ArrayList<>();
		niaoPai = new ArrayList<>();
		niaoPai.add(MajiangPai.yitiao);
		niaoPai.add(MajiangPai.yiwan);
		niaoPai.add(MajiangPai.yitong);
		niaoPai.add(MajiangPai.wutiao);
		niaoPai.add(MajiangPai.wuwan);
		niaoPai.add(MajiangPai.wutong);
		niaoPai.add(MajiangPai.jiutiao);
		niaoPai.add(MajiangPai.jiuwan);
		niaoPai.add(MajiangPai.jiutong);
	}

	public void calculate() {
		int niao = 0;
		for (MajiangPai pai : zhuaPai) {
			if (niaoPai.contains(pai)) {
				niao++;
			}
		}
		value = niao;
	}

	public int jiesuan() {
		return value;
	}

	public List<MajiangPai> getZhuaPai() {
		return zhuaPai;
	}

	public void setZhuaPai(List<MajiangPai> zhuaPai) {
		this.zhuaPai = zhuaPai;
	}

	public List<MajiangPai> getNiaoPai() {
		return niaoPai;
	}

	public void setNiaoPai(List<MajiangPai> niaoPai) {
		this.niaoPai = niaoPai;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
