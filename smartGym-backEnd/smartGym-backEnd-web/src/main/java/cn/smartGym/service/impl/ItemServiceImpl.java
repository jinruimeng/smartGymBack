package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.ItemMapper;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.ItemExample;
import cn.smartGym.pojo.ItemExample.Criteria;
import cn.smartGym.service.ItemService;
import common.enums.ErrorCode;
import common.enums.GenderGroup;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 比赛项目管理Service
 * 
 * @author ikangkang
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;

	/**
	 * 添加比赛项目功能
	 * 
	 * @param item 添加的项目
	 * @return 返回给前端的信息
	 */
	public SGResult addItem(Item item) {
		// 检查数据合法性
		if (StringUtils.isBlank(item.getGame()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "赛事不能为空！");
		if (StringUtils.isBlank(item.getCategory()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "分类不能为空！");
		if (StringUtils.isBlank(item.getItem()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "项目不能为空！");
		if (item.getGender() == null || StringUtils.isBlank(item.getGender().toString()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "性别不能为空！");
		if (item.getPathNum() == null || StringUtils.isBlank(item.getPathNum().toString()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "赛道数不能为空！");
		if (item.getDate() == null || StringUtils.isBlank(item.getDate().toString()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "比赛日期不能为空！");
		if (StringUtils.isBlank(item.getPlace()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "比赛地点不能为空！");

		List<Item> items = getItemsByDetailsAndStatuses(item);
		if (items != null && items.size() > 0)
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "该项目已存在，请先删除！", items);

		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		// 补全item其他属性
		if (item.getId() == null)
			item.setId(itemId);
		item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		itemMapper.insert(item);

		// 返回成功
		return addItemExcludesPlayers(item);
	}

	/**
	 * 增加非运动员的项目
	 * 
	 */
	public SGResult addItemExcludesPlayers(Item item) {
		item.setCategory("非运动员");
		item.setItem("非运动员");
		item.setGender(2);
		List<Item> list = getItemsByDetailsAndStatuses(item, 1);
		if (list == null || list.isEmpty()) {
			long itemId = IDUtils.genId();
			item.setId(itemId);
			item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
			item.setCreated(new Date());
			item.setUpdated(new Date());
			// 插入数据库
			itemMapper.insert(item);
		}

		// 返回成功
		return SGResult.ok("添加项目成功!");
	}

	/**
	 * 硬删除状态为（0）的项目表
	 * 
	 */
	@Override
	public void hardDeleteItem() {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<Item> itemList = itemMapper.selectByExample(example);
		for (Item item : itemList) {
			itemMapper.deleteByPrimaryKey(item.getId());
		}
	}

	/**
	 * 删除项目——根据项目的具体信息
	 * 
	 * @param itemToBeDeleted 要删除的项目
	 */
	@Override
	public SGResult deleteItem(Item itemToBeDeleted) {
		List<Item> items = getItemsByDetailsAndStatuses(itemToBeDeleted);
		if (items == null || items.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未找到要删除的项目！");

		for (Item item : items) {
			item.setStatus(0);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return SGResult.ok("删除项目成功！");
	}

	/**
	 * 维护项目表（将已结束的项目的状态设置为“已结束状态”）
	 * 
	 */
	@Override
	public void maintenanceItem() {
		Date date = new Date();
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusBetween(1, 2);
		criteria.andDateLessThan(date);
		List<Item> list = itemMapper.selectByExample(example);

		for (Item item : list) {
			item.setStatus(3);
			// 0-已取消，1-正在报名，2-报名已结束，3-比赛已结束
			item.setUpdated(date);
			itemMapper.updateByPrimaryKeySelective(item);
		}
	}

	/**
	 * 校级管理员审核某个项目（关闭报名）
	 */
	@Override
	public List<Long> reviewByUniversityManager(Item itemToBeChecked) {
		itemToBeChecked.setStatus(1);

		List<Item> items = getItemsByDetailsAndStatuses(itemToBeChecked);

		List<Long> itemIds = new ArrayList<>();

		for (Item item : items) {
			item.setStatus(2);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
			itemIds.add(item.getId());
		}
		return itemIds;
	}

	/**
	 * 根据itemId获得赛道数
	 * 
	 * @param itemId 要查找的项目id
	 */
	@Override
	public Integer getPathNumberByItemId(Long itemId) {
		Item item = itemMapper.selectByPrimaryKey(itemId);
		return item.getPathNum();
	}

	/**
	 * 查找item——根据itemId和状态status
	 * 
	 * @param itemId 要找的项目id
	 * @param status 状态
	 */
	public Item getItemByItemIdAndStatuses(Long itemId, Integer... statuses) {
		// 根据项目id和状态status查询比赛项目信息
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		if (statuses == null || statuses.length == 0 || !Arrays.asList(statuses).contains(0))
			criteria.andStatusNotEqualTo(0);
		if (statuses != null && statuses.length != 0)
			// 0-已取消 1-正在报名 2-已结束
			criteria.andStatusIn(Arrays.asList(statuses));

		List<Item> itemList = itemMapper.selectByExample(example);
		if (itemList == null || itemList.size() <= 0)
			return null;
		else
			return itemList.get(0);
	}

	/**
	 * 查找item列表——根据item详细信息
	 * 
	 * @param 要查找的项目具体信息（game,category,item)
	 */
	@Override
	public List<Item> getItemsByDetailsAndStatuses(Item item, Integer... statuses) {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		if (statuses == null || statuses.length == 0 || !Arrays.asList(statuses).contains(0))
			criteria.andStatusNotEqualTo(0);
		if (item != null) {
			if (!StringUtils.isBlank(item.getGame()))
				criteria.andGameEqualTo(item.getGame());
			if (!StringUtils.isBlank(item.getCategory()))
				criteria.andCategoryEqualTo(item.getCategory());
			if (!StringUtils.isBlank(item.getItem()))
				criteria.andItemEqualTo(item.getItem());
			if (item.getGender() != null && !StringUtils.isBlank(item.getGender().toString()))
				criteria.andGenderEqualTo(item.getGender());
		}
		if (statuses != null && statuses.length != 0)
			criteria.andStatusIn(Arrays.asList(statuses));
		// 执行查询
		List<Item> itemList = itemMapper.selectByExample(example);

		return itemList;
	}

	@Override
	public List<Long> getItemIdsByItems(List<Item> items) {
		ArrayList<Long> itemIds = new ArrayList<>();
		for (Item item : items) {
			itemIds.add(item.getId());
		}
		return itemIds;
	}

	@Override
	public Set<String> getPropertiesByItems(List<Item> items, String property) {
		// TODO 自动生成的方法存根
		Set<String> itemProperties = new HashSet<>();
		if (StringUtils.isBlank(property) || items == null)
			return itemProperties;
		switch (property) {
		case "game":
			for (Item item : items) {
				itemProperties.add(item.getGame());
			}
			break;
		case "category":
			for (Item item : items) {
				itemProperties.add(item.getCategory());
			}
			break;
		case "category1":
			for (Item item : items) {
				if (!"非运动员".equals(item.getCategory()))
					itemProperties.add(item.getCategory());
			}
			break;
		case "item":
			for (Item item : items) {
				itemProperties.add(item.getItem());
			}
			break;
		case "gender":
			for (Item item : items) {
				itemProperties.add(GenderGroup.getName(item.getGender()));
			}
			break;
		default:
			return itemProperties;
		}
		return itemProperties;
	}

}
