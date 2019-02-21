package cn.smartGym.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.pojoCtr.SgUserCtr;
import cn.smartGym.pojoCtr.response.ApplicationInfo;
import cn.smartGym.service.ApplicationService;
import cn.smartGym.service.InformService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.PlayerService;
import cn.smartGym.service.UserService;
import cn.smartGym.utils.ConversionUtils;
import common.enums.ErrorCode;
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
	private ApplicationService applicationService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private UserService userService;

	@Autowired
	private InformService informService;

	/**
	 * 根据项目查询报名人数
	 * 
	 * @param itemCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getInfoGroupByItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getInfoGroupByItem(ItemCtr itemCtr) throws Exception {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<ApplicationInfo> result = applicationService.getApplicationNumGroupByItem(items);
		return SGResult.ok("查询成功！", result);
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
	public SGResult getInfoGroupByItemDetail(Long itemId) throws Exception {
		Item item = itemService.getItemByItemIdAndStatuses(itemId);
		List<ApplicationInfo> result = applicationService.getApplicationNumGroupByItemDetail(item);
		return SGResult.ok("查询成功！", result);
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
	public SGResult getInfoGroupByCollege(ItemCtr itemCtr) throws Exception {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<ApplicationInfo> result = applicationService.getApplicationNumGroupByCollege(items);
		return SGResult.ok("查询成功！", result);
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
	public SGResult getInfoGroupByCollegeDetail(ItemCtr itemCtr, String college) throws Exception {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<ApplicationInfo> result = applicationService.getApplicationNumGroupByCollegeDetail(items, college);
		return SGResult.ok("查询成功！", result);
	}

	/**
	 * 维护数据库
	 * 
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/maintenance", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult maintenance() throws Exception {
		itemService.maintenanceItem();
		List<Item> items = itemService.getItemsByDetailsAndStatuses(null, 0, 2);
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		applicationService.maintenanceApplication(itemIds);
		return SGResult.ok("维护成功！");
	}

	/**
	 * 硬删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/hardDelete", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult hardDelete() throws Exception {
		itemService.hardDeleteItem();
		applicationService.hardDeleteApplication();
		playerService.hardDeletePlayer();
		userService.hardDeleteUser();
		informService.hardDeleteInformation();
		return SGResult.ok("硬删除成功！");
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
	public SGResult viewByCollegeManager(ItemCtr itemCtr, String college) throws Exception {
		if (StringUtils.isBlank(college))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学院不能为空！");

		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		List<Application> applications = applicationService.getApplicationListByItemIdsAndCollegeAndStatus(itemIds,
				college, 1, 2);
		// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核

		return SGResult.ok("查询院级管理员待审核名单成功！", ConversionUtils.applicationdaoListToCtrList(applications));
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
	public SGResult reviewByCollegeManager(Long[] ids) throws Exception {
		return applicationService.reviewByCollegeManager(Arrays.asList(ids));
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
	public SGResult viewByUniversityManager(ItemCtr itemCtr) throws Exception {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		List<Application> applications = applicationService.getApplicationListByItemIdsAndCollegeAndStatus(itemIds,
				"total", 1, 2, 3);
		// 0-已删除，1-等待院级管理员审核，2-等待校级管理员审核
		return SGResult.ok("查询校级管理员待审核名单成功！", ConversionUtils.applicationdaoListToCtrList(applications));
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
	public SGResult reviewByUniversityManager(ItemCtr itemCtr) throws Exception {
		// 关闭比赛报名
		List<Long> itemIds = itemService.reviewByUniversityManager(ConversionUtils.itemCtrtoDao(itemCtr));
		// 生成参赛表
		List<Application> applications = applicationService.reviewByUniversityManager(itemIds);
		SGResult sGResult = playerService.reviewByUniversityManager(applications);
		if (!sGResult.isOK())
			return sGResult;
		// 生成参赛编号
		playerService.genPlayerNo(itemIds);
		// 分组
		for (Long itemId : itemIds) {
			Integer pathNum = itemService.getPathNumberByItemId(itemId);
			playerService.genGroupNoAndPathNo(itemId, pathNum);
		}
		return SGResult.ok("校级管理员审核成功！");
	}

	/**
	 * 管理员根据学号查询用户信息
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getUser", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getUser(SgUserCtr managerUserCtr, String studentNoSelected) throws Exception {
		return userService.getUserByManagerAndStudentNos(ConversionUtils.userCtrToDao(managerUserCtr),
				studentNoSelected);
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
	public SGResult setUserAuthority(Integer authority, String[] studentNos) throws Exception {
		if (studentNos == null || studentNos.length == 0)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学号不能为空！");
		return userService.setUserAuthority(authority, studentNos);
	}

	/**
	 * 添加比赛项目
	 * 
	 * @param itemCtr
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/smartgym/manager/addItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult addItem(ItemCtr itemCtr, String dateString) throws Exception {
		// 字符串转换为日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (StringUtils.isBlank(dateString))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "日期不能为空！");
		itemCtr.setDate(sdf.parse(dateString));
		return itemService.addItem(ConversionUtils.itemCtrtoDao(itemCtr));
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
		return itemService.deleteItem(ConversionUtils.itemCtrtoDao(itemCtr));
	}

	/**
	 * 根据项目详情和学院获取报名表信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/smartgym/manager/getPlayersListByItemDetailsAndCollege", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayersListByItemDetailsAndCollege(ItemCtr itemCtr, String college) throws Exception {
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr));
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		List<Player> players = playerService.getPlayersByCollegeAndItemIds(college,
				itemIds.toArray(new Long[itemIds.size()]));

		if (players == null || players.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查到相关信息！");

		List<PlayerCtr> playersCtr = ConversionUtils.playerDaoListToCtrList(players);
		return SGResult.ok("查询成功！", playersCtr);
	}
}
