package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import cn.smartGym.service.ApplyService;
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

	/**
	 * 项目报名实现
	 * 
	 * @param applyCtr
	 * @return
	 */
	@RequestMapping(value = "/apply/addApply", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult addApply(SmartgymApplicationsCtr applyCtr) {
		// 插入数据库
		try {
			return applyService.addApply(applyCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "报名失败！", e);
		}

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
