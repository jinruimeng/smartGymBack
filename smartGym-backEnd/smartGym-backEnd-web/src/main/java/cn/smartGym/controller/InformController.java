package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymInform;
import cn.smartGym.service.InformService;
import common.utils.SGResult;

/**
 * 通知表现层
 * @author Ruimeng Jin
 *
 */
@Controller
public class InformController {

	@Autowired
	private InformService informService;
	
	@RequestMapping(value = "/inform/addInform", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult addInform(SmartgymInform smartgymInform) {
		try {
			return informService.addInform(smartgymInform);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "新增通知失败！");
		}
	}
	
	@RequestMapping(value = "/inform/updateInform", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult updateInform(SmartgymInform smartgymInform) {
		try {
			return informService.updateInform(smartgymInform);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "更新通知失败！");
		}
	}
	
	
	@RequestMapping(value = "/inform/deleteInformById", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult deleteInformById(Long id) {
		try {
			return informService.deleteInformById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "删除通知失败！");
		}
	}
	
	
	@RequestMapping(value = "/inform/getInformList", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInformList(Integer type) {
		try {
			return informService.getInformList(type);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "返回通知列表失败！");
		}
	}
	
	@RequestMapping(value = "/inform/getInformById", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInformById(Long id) {
		try {
			return informService.getInformById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查找通知信息失败！");
		}
	}
}
