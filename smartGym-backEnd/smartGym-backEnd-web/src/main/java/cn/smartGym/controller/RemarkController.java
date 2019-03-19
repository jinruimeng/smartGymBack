package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Remark;
import cn.smartGym.service.RemarkService;
import common.utils.SGResult;

@Controller
public class RemarkController {

	@Autowired
	private RemarkService remarkServic;
	
	@RequestMapping(value = "/smartgym/remark/addRemark", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	SGResult addRemark(Remark remark) {
		return remarkServic.addRemark(remark);
	}
}
