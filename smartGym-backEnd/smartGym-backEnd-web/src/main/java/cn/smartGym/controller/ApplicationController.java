package cn.smartGym.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojoCtr.ApplicationCtr;
import cn.smartGym.service.ApplicationService;
import cn.smartGym.utils.ConversionUtils;
import common.enums.ErrorCode;
import common.utils.SGResult;

/**
 * 比赛项目管理Controller
 * 
 * @author Ruimeng Jin
 *
 */
@Controller
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	/**
	 * 项目报名实现
	 * 
	 * @param applicationCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/application/addApplication", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult addApplication(ApplicationCtr applicationCtr) throws Exception {
		// 插入数据库
		return applicationService.addApplication(ConversionUtils.applicationCtrToDao(applicationCtr));

	}

	/**
	 * 根据学号获取已报名项目信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/smartgym/application/getApplicationListByStudentNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getApplicationListByStudentNo(String studentNo) throws Exception {
		if (StringUtils.isBlank(studentNo))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学号不能为空！");
		List<Application> result = applicationService.getApplicationListByStatusAndStudentNo(null, studentNo);
		return SGResult.ok("查询成功！", ConversionUtils.applicationdaoListToCtrList(result));
	}
}
