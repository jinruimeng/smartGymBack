package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Information;
import cn.smartGym.service.InformService;
import common.enums.ErrorCode;
import common.utils.SGResult;

/**
 * 通知表现层
 * 
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
	public SGResult addInform(Information Information) throws Exception {
		informService.addInform(Information);
		return SGResult.ok( "增加消息成功！");
	}

	@RequestMapping(value = "/inform/updateInform", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult updateInform(Information Information) throws Exception {
		informService.updateInform(Information);
		return SGResult.ok( "修改消息成功！");
	}

	@RequestMapping(value = "/inform/deleteInformById", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult deleteInformById(Long id) throws Exception {
		if (id == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "要删除的记录不能为空！");
		return informService.deleteInformById(id);
	}

	@RequestMapping(value = "/inform/getInformList", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInformList(Integer type) throws Exception {
		if (type == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "通知的类型不能为空！");
		return informService.getInformListByTypeAndIds(type, new Long[0]);
	}

	@RequestMapping(value = "/inform/getInformById", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInformById(Long id) throws Exception {
		return informService.getInformListByTypeAndIds(0, id);
	}
}
