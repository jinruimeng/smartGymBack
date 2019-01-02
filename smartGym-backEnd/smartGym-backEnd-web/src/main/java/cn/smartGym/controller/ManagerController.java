package cn.smartGym.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.service.ApplyService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.PlayerService;
import cn.smartGym.service.UserService;
import common.utils.SGResult;

/**
 * 管理人员Controller
 * 
 * @author Ruimeng Jin
 *
 */
@Controller
public class ManagerController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ApplyService applyService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private UserService userService;

	/**
	 * 根据项目查询报名人数
	 * 
	 * @param game
	 * @return
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/manager/getInfoGroupByItem", method = {
	 * RequestMethod.POST, RequestMethod.GET }, consumes =
	 * "application/x-www-form-urlencoded;charset=utf-8")
	 * 
	 * @ResponseBody public SGResult getInfoGroupByItem(String game) { try {
	 * List<SmartgymItemsCtr> items = itemService.getItemsByGame(game);
	 * Map<Map<String, String>, Long> result = new HashedMap(); for
	 * (SmartgymItemsCtr smartgymItemsCtr : items) { Map<String, String> itemInfo =
	 * new HashedMap(); itemInfo.put(smartgymItemsCtr.getItem(),
	 * smartgymItemsCtr.getGender()); result.put(itemInfo,
	 * applyService.countByitem(smartgymItemsCtr.getId())); } return
	 * SGResult.build(200, "查询成功！", result); } catch (Exception e) {
	 * e.printStackTrace(); return SGResult.build(404, "查询失败！", e); } }
	 */

	/**
	 * 根据项目查询报名人数
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/manager/getInfoGroupByItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByItem(SmartgymItemsCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<SmartgymItemsCtr> itemsCtr = itemService.getItemsByItemDetails(itemCtr);
			Map<Map<Map<String, String>, String>, Long> result = applyService.getApplyNumGroupByItem(itemsCtr);
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}
	}

	/**
	 * 根据学院查询报名人数
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/manager/getInfoGroupByCollege", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByCollege(SmartgymItemsCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<SmartgymItemsCtr> itemsCtr = itemService.getItemsByItemDetails(itemCtr);
			Map<Map<String, Map<String, String>>, Long> result = applyService.getApplyNumGroupByCollege(itemsCtr);
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}
	}

	/**
	 * 维护项目表和报名表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/manager/maintenance", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult maintenance() {
		try {
			itemService.maintenanceItem();
			applyService.maintenanceApply(itemService.selectItemByStatus(0, 2));
			return SGResult.build(200, "维护成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "维护失败！", e);
		}
	}

	/**
	 * 硬删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/manager/hardDelete", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult hardDelete() {
		try {
			itemService.hardDeleteItem();
			applyService.hardDeleteApply();
			playerService.hardDeletePlayer();
			userService.hardDeleteUser();
			return SGResult.build(200, "硬删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "硬删除失败！", e);
		}
	}

	/**
	 * 院级管理员待审核名单显示
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/manager/viewByCollegeManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult viewByCollegeManager(SmartgymItemsCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			List<SmartgymApplications> applycations = applyService.getApplycationListByItemsId(itemsId, 1);
			// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核
			return SGResult.build(200, "查询院级管理员待审核名单成功！", applycations);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询院级管理员待审核名单失败！", e);
		}
	}

	/**
	 * 院级管理员审核
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/manager/reviewByCollegeManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult reviewByCollegeManager(Long ids[]) {
		try {
			return applyService.reviewByCollegeManager(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "院级管理员审核失败！", e);
		}
	}

	/**
	 * 校级管理员待审核名单显示
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/manager/viewByUniversityManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult viewByUniversityManager(SmartgymItemsCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			List<SmartgymApplications> applycations = applyService.getApplycationListByItemsId(itemsId, 2);
			// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核
			return SGResult.build(200, "查询校级管理员待审核名单成功！", applycations);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询校级管理员待审核名单失败！", e);
		}
	}

	/**
	 * 校级管理员审核
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/manager/reviewByUniversityManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult reviewByUniversityManager(Long[] ids) {
		try {
			List<SmartgymApplications> applications = applyService.reviewByUniversityManager(ids);
			return playerService.reviewByUniversityManager(applications);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "院级管理员审核失败！", e);
		}
	}

	/**
	 * 生成参赛号
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/manager/genPlayerNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult genPlayerNo(SmartgymItemsCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			return playerService.genPlayerNo(itemsId);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "生成参赛号失败！", e);
		}
	}

}
