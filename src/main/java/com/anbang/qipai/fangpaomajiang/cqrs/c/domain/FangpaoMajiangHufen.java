package com.anbang.qipai.fangpaomajiang.cqrs.c.domain;

/**
 * 结算规则 自摸：所有人跟胡家结算胡牌牌型分数；杠分单独结算 放炮：其他玩家不用与胡家结算，只有放炮的人与胡家结算胡牌牌型分数；杠分单独结算
 * 
 * @author lsc
 *
 */
public class FangpaoMajiangHufen {
	private boolean hu;
	private boolean zimoHu;// 每人输2分；
	private boolean qiangganghu;// 抢杠胡：2分；
	private boolean qiduihu;// 七对：4分；
	private boolean pengpenghu;// 碰碰胡：4分；
	private boolean qingyise;// 清一色：4分；
	private boolean gangshangkaihua;// 杠上开花：4分；
	private boolean danzhangdiao;// 单张吊：6分；
	private boolean caishendiao;// 财神吊8分；
	private boolean tianhu;// 天胡：8分；
	private boolean dihu;// 地胡：8分；
	private boolean qiduiqingyise;// 七对清一色：10分
	private boolean qiduigangkai;// 七对杠开：10分
	private boolean qiduidanzhangdiao;// 七对单张吊：10分
	private boolean qingyisepengpenghu;// 清一色碰碰胡：10分
	private boolean qingyidegangkai;// 清一色杠开：10分
	private boolean qingyisedanzhangdiao;// 清一色单张吊：10分
	private int value;

	public void calculate() {
		int hushu = 0;
		if (hu) {
			hushu = 1;
		}
		if (zimoHu) {
			hushu = 2;
		}
		if (qiangganghu) {
			hushu = 2;
		}
		if (qiduihu) {
			hushu = 4;
		}
		if (pengpenghu) {
			hushu = 4;
		}
		if (qingyise) {
			hushu = 4;
		}
		if (gangshangkaihua) {
			hushu = 4;
		}
		if (danzhangdiao) {
			hushu = 6;
		}
		if (caishendiao) {
			hushu = 8;
		}
		if (tianhu) {
			hushu = 8;
		}
		if (dihu) {
			hushu = 8;
		}
		if (qiduiqingyise) {
			hushu = 10;
		}
		if (qiduigangkai) {
			hushu = 10;
		}
		if (qiduidanzhangdiao) {
			hushu = 10;
		}
		if (qingyisepengpenghu) {
			hushu = 10;
		}
		if (qingyidegangkai) {
			hushu = 10;
		}
		if (qingyisedanzhangdiao) {
			hushu = 10;
		}
		value = hushu;
	}

	public int jiesuan(int delta) {
		return value += delta;
	}

	public boolean isQiangganghu() {
		return qiangganghu;
	}

	public void setQiangganghu(boolean qiangganghu) {
		this.qiangganghu = qiangganghu;
	}

	public boolean isQiduihu() {
		return qiduihu;
	}

	public void setQiduihu(boolean qiduihu) {
		this.qiduihu = qiduihu;
	}

	public boolean isPengpenghu() {
		return pengpenghu;
	}

	public void setPengpenghu(boolean pengpenghu) {
		this.pengpenghu = pengpenghu;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public void setQingyise(boolean qingyise) {
		this.qingyise = qingyise;
	}

	public boolean isGangshangkaihua() {
		return gangshangkaihua;
	}

	public void setGangshangkaihua(boolean gangshangkaihua) {
		this.gangshangkaihua = gangshangkaihua;
	}

	public boolean isDanzhangdiao() {
		return danzhangdiao;
	}

	public void setDanzhangdiao(boolean danzhangdiao) {
		this.danzhangdiao = danzhangdiao;
	}

	public boolean isCaishendiao() {
		return caishendiao;
	}

	public void setCaishendiao(boolean caishendiao) {
		this.caishendiao = caishendiao;
	}

	public boolean isTianhu() {
		return tianhu;
	}

	public void setTianhu(boolean tianhu) {
		this.tianhu = tianhu;
	}

	public boolean isDihu() {
		return dihu;
	}

	public void setDihu(boolean dihu) {
		this.dihu = dihu;
	}

	public boolean isQiduiqingyise() {
		return qiduiqingyise;
	}

	public void setQiduiqingyise(boolean qiduiqingyise) {
		this.qiduiqingyise = qiduiqingyise;
	}

	public boolean isQiduigangkai() {
		return qiduigangkai;
	}

	public void setQiduigangkai(boolean qiduigangkai) {
		this.qiduigangkai = qiduigangkai;
	}

	public boolean isQiduidanzhangdiao() {
		return qiduidanzhangdiao;
	}

	public void setQiduidanzhangdiao(boolean qiduidanzhangdiao) {
		this.qiduidanzhangdiao = qiduidanzhangdiao;
	}

	public boolean isQingyisepengpenghu() {
		return qingyisepengpenghu;
	}

	public void setQingyisepengpenghu(boolean qingyisepengpenghu) {
		this.qingyisepengpenghu = qingyisepengpenghu;
	}

	public boolean isQingyidegangkai() {
		return qingyidegangkai;
	}

	public void setQingyidegangkai(boolean qingyidegangkai) {
		this.qingyidegangkai = qingyidegangkai;
	}

	public boolean isQingyisedanzhangdiao() {
		return qingyisedanzhangdiao;
	}

	public void setQingyisedanzhangdiao(boolean qingyisedanzhangdiao) {
		this.qingyisedanzhangdiao = qingyisedanzhangdiao;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isZimoHu() {
		return zimoHu;
	}

	public void setZimoHu(boolean zimoHu) {
		this.zimoHu = zimoHu;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

}
