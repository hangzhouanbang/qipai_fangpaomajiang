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
		if (hufen.isQingyisedanzhangdiao()) {
			qingyisedanzhangdiao = true;
		} else if (hufen.isQingyisegangkai()) {
			qingyidegangkai = true;
		} else if (hufen.isQingyisepengpenghu()) {
			qingyisepengpenghu = true;
		} else if (hufen.isQiduiqingyise()) {
			qiduiqingyise = true;
		} else if (hufen.isDihu()) {
			dihu = true;
		} else if (hufen.isTianhu()) {
			tianhu = true;
		} else if (hufen.isCaishendiao()) {
			caishendiao = true;
		} else if (hufen.isDanzhangdiao()) {
			danzhangdiao = true;
		} else if (hufen.isGangshangkaihua()) {
			gangshangkaihua = true;
		} else if (hufen.isQingyise()) {
			qingyise = true;
		} else if (hufen.isPengpenghu()) {
			pengpenghu = true;
		} else if (hufen.isQiduihu()) {
			qiduihu = true;
		} else if (hufen.isQiangganghu()) {
			qiangganghu = true;
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
