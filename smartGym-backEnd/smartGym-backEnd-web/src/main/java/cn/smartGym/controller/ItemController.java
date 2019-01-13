package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojoctr.request.ItemCtr;
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

	/**
	 * 获取正在报名的项目信息
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getInfo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage(ItemCtr itemsCtr) {
		List<String> result = itemService.getNameByDetailsAndStatus(itemsCtr, 1);
		// 0-已删除，1-正在报名，2-报名结束
		if (result == null || result.size() == 0) {
			SGResult.build(404, "获取项目信息失败！");
		}
		return SGResult.build(200, "获取项目信息成功!", result);
	}

}
