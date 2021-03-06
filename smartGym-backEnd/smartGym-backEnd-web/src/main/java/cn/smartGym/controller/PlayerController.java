package cn.smartGym.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.PlayerService;
import cn.smartGym.utils.ConversionUtils;
import common.enums.ErrorCode;
import common.utils.SGResult;

/**
 * 参赛人员管理Controller
 * 
 * @author ikangkang
 *
 */
@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	@Autowired
	private ItemService itemService;

	/**
	 * 根据学号获取参赛表信息
	 * 
	 * @param studentNo
	 * @return
	 */
	@RequestMapping(value = "/smartgym/player/getPlayerListByStudentNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayerListByStudentNo(String studentNo) throws Exception {
		if (StringUtils.isBlank(studentNo))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学号不能为空！");

		List<Player> players = playerService.getPlayersByStudentNo(studentNo);

		if (players == null || players.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");

		List<PlayerCtr> result = ConversionUtils.playerDaoListToCtrList(players);

		return SGResult.ok("查询成功！", result);

	}

	/**
	 * 根据某一项目下的参赛表信息
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/smartgym/player/getPlayerListByItem", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayerListByItem(ItemCtr itemCtr) {
		// 获取项目列表
		List<Item> items = itemService.getItemsByDetailsAndStatuses(ConversionUtils.itemCtrtoDao(itemCtr), 2, 3);

		if (items == null || items.isEmpty())
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "数据库中无相关信息！");

		// 获取项目Id
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		// 获取参赛表
		List<Player> players = playerService.getPlayersByCollegeAndItemIds("total",
				itemIds.toArray(new Long[itemIds.size()]));

		if (players == null || players.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "数据库中无相关信息！");

		List<PlayerCtr> result = ConversionUtils.playerDaoListToCtrList(players);

		return SGResult.ok("查询成功！", result);

	}

}
