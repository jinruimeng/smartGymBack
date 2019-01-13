package cn.smartGym.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojoctr.request.ApplicationCtr;
import cn.smartGym.pojoctr.request.ItemCtr;
import cn.smartGym.pojoctr.request.UserCtr;
import cn.smartGym.pojoctr.response.ApplicationInfo;
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
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getInfoGroupByItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByItem(ItemCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<ItemCtr> itemsCtr = itemService.getItemsCtrByItemDetails(itemCtr);
			List<ApplicationInfo> result = applyService.getApplyNumGroupByItem(itemsCtr);
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}
	}

	/**
	 * 根据项目查询报名人数(详细)
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getInfoGroupByItemDetail", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByItemDetail(Long itemId) {
		try {
			ItemCtr itemCtr = itemService.getItemByItemId(itemId, 1);
			List<ApplicationInfo> result = applyService.getApplyNumGroupByItemDetail(itemCtr);
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
	@RequestMapping(value = "/smartgym/manager/getInfoGroupByCollege", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByCollege(ItemCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<ItemCtr> itemsCtr = itemService.getItemsCtrByItemDetails(itemCtr);
			List<ApplicationInfo> result = applyService.getApplyNumGroupByCollege(itemsCtr);
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}
	}

	/**
	 * 根据学院查询报名人数（详细）
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getInfoGroupByCollegeDetail", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByCollegeDetail(ItemCtr itemCtr, String college) {
		try {
			itemCtr.setStatus(1);
			List<ItemCtr> itemsCtr = itemService.getItemsCtrByItemDetails(itemCtr);
			List<ApplicationInfo> result = applyService.getApplyNumGroupByCollegeDetail(itemsCtr, college);
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
	@RequestMapping(value = "/smartgym/manager/maintenance", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult maintenance() {
		try {
			itemService.maintenanceItem();
			applyService.maintenanceApply(itemService.selectItemByStatus(0, 3));
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
	@RequestMapping(value = "/smartgym/manager/hardDelete", method = { RequestMethod.POST,
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
	@RequestMapping(value = "/smartgym/manager/viewByCollegeManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult viewByCollegeManager(ItemCtr itemCtr, String college) {
		try {
			if (StringUtils.isBlank(college))
				return SGResult.build(200, "学院不能为空！");

			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			List<ApplicationCtr> applicationsCtr = applyService.getApplicationListByItemsId(itemsId, 1, college);
			// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核

			return SGResult.build(200, "查询院级管理员待审核名单成功！", applicationsCtr);
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
	@RequestMapping(value = "/smartgym/manager/reviewByCollegeManager", method = { RequestMethod.POST,
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
	@RequestMapping(value = "/smartgym/manager/viewByUniversityManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult viewByUniversityManager(ItemCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			List<ApplicationCtr> applicationsCtr = applyService.getApplicationListByItemsId(itemsId, 2, null);
			// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核

			return SGResult.build(200, "查询校级管理员待审核名单成功！", applicationsCtr);
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
	@RequestMapping(value = "/smartgym/manager/reviewByUniversityManager", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult reviewByUniversityManager(ItemCtr itemCtr) {
		try {
			// 关闭比赛报名
			List<Long> itemsId = itemService.reviewByUniversityManage(itemCtr);
			// 生成参赛表
			List<Application> applications = applyService.reviewByUniversityManager(itemsId);
			playerService.reviewByUniversityManager(applications);
			// 生成参赛编号
			playerService.genPlayerNo(itemsId);
			// 分组
			for (Long itemId : itemsId) {
				Integer pathNum = itemService.getPathNumberByItemId(itemId);
				playerService.genGroupNoAndPathNo(itemId,pathNum);
			}
			return SGResult.build(200, "校级管理员审核成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "校级管理员审核失败！", e);
		}
	}

	/**
	 * 生成参赛号
	 * 
	 * @param ids
	 * @return
	 */
/*	@RequestMapping(value = "/smartgym/manager/genPlayerNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult genPlayerNo(ItemCtr itemCtr) {
		try {
			itemCtr.setStatus(1);
			List<Long> itemsId = itemService.getItemIdByItemDetails(itemCtr);
			return playerService.genPlayerNo(itemsId);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "生成参赛号失败！", e);
		}
	}*/

	/**
	 * 生成参赛队员分组和赛道
	 * 
	 * @param usersCtr
	 * @return
	 */
/*	@RequestMapping(value = "/smartgym/manager/genGroupNoAndPathNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult genGroupNoAndPathNo(Long itemId, Integer number) {
		try {
			return playerService.genGroupNoAndPathNo(itemId, number);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "设置参赛队员分组和赛道失败！");
		}
	}*/

	/**
	 * 管理员根据学号查询用户信息
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getUser", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getUser(UserCtr userCtr, String studentNoSelected) {
		try {
			return userService.getUser(userCtr, studentNoSelected);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询用户失败！");
		}
	}

	/**
	 * 设置用户权限
	 * 
	 * @param usersCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/setUserAuthority", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult setUserAuthority(String studentNo, Integer authority) {
		try {
			return userService.setUserAuthority(studentNo, authority);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "设置用户权限失败！");
		}
	}
	
	/**
	 * 添加比赛项目
	 * 
	 * @param itemCtr
	 * @param dateString
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/addItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult addItem(ItemCtr itemCtr, String dateString) {
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
	@RequestMapping(value = "/smartgym/manager/deleteItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult deleteItem(ItemCtr itemCtr) {
		try {
			return itemService.deleteItem(itemCtr);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "删除项目失败！", e);
		}
	}


}
