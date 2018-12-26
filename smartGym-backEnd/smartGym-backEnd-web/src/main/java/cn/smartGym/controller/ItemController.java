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

	@RequestMapping(value = "/item/add", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult itemAdd(SmartgymItemsCtr itemCtr) {
		return itemService.addItem(itemService.itemCtrToDao(itemCtr));
	}
}
