package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		// 插入数据库
		try {
			return applyService.addApply(applyCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "报名失败！", e);
		}

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
		List<String> result = itemService.applySelect(itemsCtr);
		if (result == null || result.size() == 0) {
			SGResult.build(404, "获取项目信息失败！");
		}
		return SGResult.build(200, "获取项目信息成功!", result);
	}

	/**
	 * 根据学号获取已报名项目信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/apply/getApplycationListByStudentNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getApplycationListByStudentNo(String studentNo) {
		try {
			List<SmartgymApplicationsCtr> result = applyService.getApplycationListByStudentNo(studentNo);
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}

	}
}
