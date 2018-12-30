package cn.smartGym.controller;

import java.text.SimpleDateFormat;

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
	public SGResult addItem(SmartgymItemsCtr itemCtr, String dateString) {
		try {
			// 字符串转换为日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			itemCtr.setDate(sdf.parse(dateString));
			return itemService.addItem(itemCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "添加项目失败！", e);
		}
	}

	@RequestMapping(value = "/item/deleteItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult deleteItem(SmartgymItemsCtr itemCtr) {
		try {
			return itemService.deleteItem(itemCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "删除项目失败！", e);
		}
	}

}
