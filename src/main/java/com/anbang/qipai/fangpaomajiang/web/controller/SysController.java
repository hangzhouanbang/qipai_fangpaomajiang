package com.anbang.qipai.fangpaomajiang.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.fangpaomajiang.web.vo.CommonVO;
import com.dml.majiang.player.shoupai.gouxing.GouXingCalculator;
import com.dml.majiang.player.shoupai.gouxing.GouXingCalculatorHelper;

@RestController
@RequestMapping("/sys")
public class SysController {

	@RequestMapping(value = "/tgxc")
	@ResponseBody
	public CommonVO takeGouXingCalculator(HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		if (GouXingCalculatorHelper.gouXingCalculator != null) {
			vo.setSuccess(false);
			return vo;
		}
		ServletContext sc = request.getServletContext().getContext("/majiang-gouxing-service");
		GouXingCalculatorHelper.gouXingCalculator = (GouXingCalculator) sc.getAttribute("14_4");
		return vo;
	}

}
