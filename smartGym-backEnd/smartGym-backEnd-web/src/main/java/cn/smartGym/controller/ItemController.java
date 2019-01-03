package cn.smartGym.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	/**
	 * 获取正在报名的项目信息
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/item/getInfo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage(SmartgymItemsCtr itemsCtr) {
		List<String> result = itemService.getNameByDetailsAndStatus(itemsCtr, 1);
		// 0-已删除，1-正在报名，2-报名结束
		if (result == null || result.size() == 0) {
			SGResult.build(404, "获取项目信息失败！");
		}
		return SGResult.build(200, "获取项目信息成功!", result);
	}

	/**
	 * 添加比赛项目
	 * 
	 * @param itemCtr
	 * @param dateString
	 * @return
	 */
	@RequestMapping(value = "/item/addItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult addItem(SmartgymItemsCtr itemCtr, String dateString) {
		try {
			// 字符串转换为日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (StringUtils.isBlank(dateString))
				return SGResult.build(200, "日期不能为空！");
			itemCtr.setDate(sdf.parse(dateString));
			return itemService.addItem(itemCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "添加项目失败！", e);
		}
	}

	/**
	 * 删除比赛项目
	 * 
	 * @param itemCtr
	 * @return
	 */
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
