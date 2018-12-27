package cn.smartGym.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.service.ApplyService;
import cn.smartGym.service.ItemService;
import common.utils.SGResult;

/**
 * 比赛项目管理Controller
 * 
 * @author Ruimeng Jin
 *
 */
@Controller
public class ApplyController {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private ItemService itemService;

	/**
	 * 项目报名实现
	 * 
	 * @param applyCtr
	 * @return
	 */
	@RequestMapping(value = "/apply/addapply", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult addapply(SmartgymApplicationsCtr applyCtr) {
		SmartgymApplications apply = applyService.applyCtrtoDao(applyCtr);
		if (apply == null)
			SGResult.build(401, "报名项目有误！");
		// 插入数据库
		return applyService.addApply(apply);
	}

	/**
	 * 项目报名页面数据回传
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/apply/applypage", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult applypage(SmartgymItemsCtr itemsCtr) {
		ArrayList<String> result = itemService.applySelect(itemsCtr);
		if (result == null || result.size() == 0) {
			SGResult.build(401, "无法获取项目！");
		}
		return SGResult.build(200, "获取项目信息成功!", result);
	}

	@RequestMapping(value = "/apply/getApplycationListByStudentno", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public List<SmartgymApplicationsCtr> getApplycationListByStudentno(String studentno) {
		return applyService.getApplycationListByStudentno(studentno);
	}
}
