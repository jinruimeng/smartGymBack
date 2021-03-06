package cn.smartGym.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Item;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.service.ItemService;
import cn.smartGym.utils.ConversionUtils;
import common.enums.ErrorCode;
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
	 * 查询全部比赛（状态1、2、3）
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getItemInfo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getItemInfo(ItemCtr itemCtr) {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 1, 2, 3);
		if (items == null || items.size() == 0) {
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");
		}
		return SGResult.ok("获取项目信息成功!", ConversionUtils.itemDaoListToCtrList(items));
	}

	/**
	 * 获取正在报名的项目信息（项目状态为1）（包括非运动员）
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getInfo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage(ItemCtr itemCtr) {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 1);
		if (items == null || items.size() == 0) {
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");
		}
		Set<String> result = new HashSet<>();
		// 0-已删除，1-正在报名，2-报名结束，3-比赛结束
		if (!StringUtils.isBlank(itemCtr.getItem()))
			result = itemService.getPropertiesByItems(items, "gender");
		else if (!StringUtils.isBlank(itemCtr.getCategory()))
			result = itemService.getPropertiesByItems(items, "item");
		else if (!StringUtils.isBlank(itemCtr.getGame()))
			result = itemService.getPropertiesByItems(items, "category");
		else
			result = itemService.getPropertiesByItems(items, "game");

		return SGResult.ok("获取项目信息成功!", result);
	}

	/**
	 * 获取正在报名的项目信息（项目状态为1）（排除非运动员）
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getInfo1", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage1(ItemCtr itemCtr) {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 1);
		if (items == null || items.size() == 0) {
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");
		}
		Set<String> result = new HashSet<>();
		// 0-已删除，1-正在报名，2-报名结束，3-比赛结束
		if (!StringUtils.isBlank(itemCtr.getItem()))
			result = itemService.getPropertiesByItems(items, "gender");
		else if (!StringUtils.isBlank(itemCtr.getCategory()))
			result = itemService.getPropertiesByItems(items, "item");
		else if (!StringUtils.isBlank(itemCtr.getGame()))
			result = itemService.getPropertiesByItems(items, "category1");
		else
			result = itemService.getPropertiesByItems(items, "game");

		return SGResult.ok("获取项目信息成功!", result);
	}

	/**
	 * 获取报名结束的项目信息（项目状态2和3）（包括非运动员）
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getInfo2", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage2(ItemCtr itemCtr) {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 2, 3);
		// 0-已删除，1-正在报名，2-报名结束，3-比赛结束
		Set<String> result = new HashSet<>();
		if (items == null || items.size() == 0) {
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");
		}
		if (!StringUtils.isBlank(itemCtr.getItem()))
			result = itemService.getPropertiesByItems(items, "gender");
		else if (!StringUtils.isBlank(itemCtr.getCategory()))
			result = itemService.getPropertiesByItems(items, "item");
		else if (!StringUtils.isBlank(itemCtr.getGame()))
			result = itemService.getPropertiesByItems(items, "category");
		else {
			result = itemService.getPropertiesByItems(items, "game");
		}
		return SGResult.ok("获取项目信息成功!", result);
	}

	/**
	 * 登记成绩时，获取项目信息（项目状态2和3）（排除非运动员）
	 * 
	 * @param itemsCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/item/getInfo3", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoPage3(ItemCtr itemCtr) {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 2, 3);
		// 0-已删除，1-正在报名，2-报名结束，3-比赛结束
		Set<String> result = new HashSet<>();
		if (items == null || items.size() == 0) {
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");
		}
		if (!StringUtils.isBlank(itemCtr.getItem()))
			result = itemService.getPropertiesByItems(items, "gender");
		else if (!StringUtils.isBlank(itemCtr.getCategory()))
			result = itemService.getPropertiesByItems(items, "item");
		else if (!StringUtils.isBlank(itemCtr.getGame()))
			result = itemService.getPropertiesByItems(items, "category1");
		else {
			result = itemService.getPropertiesByItems(items, "game");
		}
		return SGResult.ok("获取项目信息成功!", result);
	}

}
