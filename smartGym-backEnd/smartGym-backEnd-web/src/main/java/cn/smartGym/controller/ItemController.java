package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.service.ItemService;
import common.utils.SGResult;

/**
 * 比赛项目管理Controller
 * 
 * @author ikangkang
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/item/addItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult addItem(SmartgymItemsCtr itemCtr) {
		try {
			return itemService.addItem(itemCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "添加比赛失败！", e);
		}

	}
}
