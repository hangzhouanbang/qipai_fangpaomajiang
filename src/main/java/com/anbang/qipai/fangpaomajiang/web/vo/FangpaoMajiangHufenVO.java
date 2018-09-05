package com.anbang.qipai.fangpaomajiang.web.vo;

import com.anbang.qipai.fangpaomajiang.cqrs.c.domain.FangpaoMajiangHufen;

public class FangpaoMajiangHufenVO {
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
	private boolean qingyisepengpenghu;// 清一色碰碰胡：10分
	private boolean qingyidegangkai;// 清一色杠开：10分
	private boolean qingyisedanzhangdiao;// 清一色单张吊：10分

	public FangpaoMajiangHufenVO() {

	}

	public FangpaoMajiangHufenVO(FangpaoMajiangHufen hufen) {
		qiangganghu = hufen.isQiangganghu();
		if (hufen.isQiduihu()) {
			qiangganghu = false;
			qiduihu = hufen.isQiduihu();
		}
		if (hufen.isPengpenghu()) {
			qiduihu = false;
			pengpenghu = hufen.isPengpenghu();
		}
		if (hufen.isQingyise()) {
			pengpenghu = false;
			qingyise = hufen.isQingyise();
		}
		if (hufen.isGangshangkaihua()) {
			qingyise = false;
			gangshangkaihua = hufen.isGangshangkaihua();
		}
		if (hufen.isDanzhangdiao()) {
			gangshangkaihua = false;
			danzhangdiao = hufen.isDanzhangdiao();
		}
		if (hufen.isCaishendiao()) {
			danzhangdiao = false;
			caishendiao = hufen.isCaishendiao();
		}
		if (hufen.isTianhu()) {
			caishendiao = false;
			tianhu = hufen.isTianhu();
		}
		if (hufen.isDihu()) {
			tianhu = false;
			dihu = hufen.isDihu();
		}
		if (hufen.isQiduiqingyise()) {
			dihu = false;
			qiduiqingyise = hufen.isQiduiqingyise();
		}
		if (hufen.isQingyisepengpenghu()) {
			qiduiqingyise = false;
			qingyisepengpenghu = hufen.isQingyisepengpenghu();
		}
		if (hufen.isQingyidegangkai()) {
			qingyisepengpenghu = false;
			qingyidegangkai = hufen.isQingyidegangkai();
		}
		if (hufen.isQingyisedanzhangdiao()) {
			qingyidegangkai = false;
			qingyisedanzhangdiao = hufen.isQingyisedanzhangdiao();
		}
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
}
