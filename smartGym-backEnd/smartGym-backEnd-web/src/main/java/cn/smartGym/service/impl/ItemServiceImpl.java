package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.controller.pojoCtr.ItemCtr;
import cn.smartGym.mapper.ItemMapper;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.ItemExample;
import cn.smartGym.pojo.ItemExample.Criteria;
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.ItemService;
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

	@Autowired
	private GenderGroupService genderGroupService;

	/**
	 * Controller-Dao层接收bean转换器
	 * 
	 * @param itemCtr
	 *            接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	public Item itemCtrToDao(ItemCtr itemCtr) {
		Item item = new Item();
		item.setGame(itemCtr.getGame());
		item.setCategory(itemCtr.getCategory());
		item.setItem(itemCtr.getItem());
		item.setDate(itemCtr.getDate());
		item.setPlace(itemCtr.getPlace());
		item.setParticipantNum(itemCtr.getParticipantNum());
		item.setPathNum(itemCtr.getPathNum());
		item.setDescription(itemCtr.getDescription());
		item.setGender(genderGroupService.genderStrToInt(itemCtr.getGender()));
		if (itemCtr.getId() != null)
			item.setId(itemCtr.getId());
		if (itemCtr.getStatus() != null)
			item.setStatus(itemCtr.getStatus());

		return item;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * 
	 * @param item
	 *            从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	public ItemCtr itemDaoToCtr(Item item) {
		ItemCtr itemCtr = new ItemCtr();
		itemCtr.setId(item.getId());
		itemCtr.setGame(item.getGame());
		itemCtr.setCategory(item.getCategory());
		itemCtr.setItem(item.getItem());
		itemCtr.setDate(item.getDate());
		itemCtr.setPlace(item.getPlace());
		itemCtr.setParticipantNum(item.getParticipantNum());
		itemCtr.setPathNum(item.getPathNum());
		itemCtr.setDescription(item.getDescription());
		itemCtr.setGender(genderGroupService.genderIntToStr(item.getGender()));
		if (item.getId() != null)
			itemCtr.setId(item.getId());
		if (item.getStatus() != null)
			itemCtr.setStatus(item.getStatus());

		return itemCtr;
	}

	/**
	 * 添加比赛项目功能
	 * 
	 * @param item
	 *            添加的项目
	 * @return 返回给前端的信息
	 */
	public SGResult addItem(Item item) {
		// 检查数据合法性
		if (StringUtils.isBlank(item.getGame()))
			return SGResult.build(200, "赛事不能为空！");
		if (StringUtils.isBlank(item.getCategory()))
			return SGResult.build(200, "分类不能为空！");
		if (StringUtils.isBlank(item.getItem()))
			return SGResult.build(200, "项目不能为空！");
		if (item.getGender() == null || StringUtils.isBlank(item.getGender().toString()))
			return SGResult.build(200, "性别不能为空！");
		if (item.getPathNum() == null || StringUtils.isBlank(item.getPathNum().toString()))
			return SGResult.build(200, "赛道数不能为空！");
		if (item.getDate() == null || StringUtils.isBlank(item.getDate().toString()))
			return SGResult.build(200, "比赛日期不能为空！");
		if (StringUtils.isBlank(item.getPlace()))
			return SGResult.build(200, "比赛地点不能为空！");

		List<Item> items = getItemsByItemDetails(item);
		if (items != null && items.size() > 0)
			return SGResult.build(200, "该项目已存在，请先删除！", items);

		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		// 补全item其他属性
		if (item.getStatus() == null)
			item.setId(itemId);
		item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		itemMapper.insert(item);
		// 返回成功
		return SGResult.build(200, "添加项目成功!");
	}

	/**
	 * 删除项目——根据项目的具体信息
	 * 
	 * @param itemToBeDeleted
	 *            要删除的项目
	 */
	@Override
	public SGResult deleteItem(Item itemToBeDeleted) {
		List<Item> items = getItemsByItemDetails(itemToBeDeleted);
		if (items == null || items.size() <= 0)
			return SGResult.build(200, "未找到要删除的项目！");

		for (Item item : items) {
			item.setStatus(0);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return SGResult.build(200, "删除成功！");
	}

	/**
	 * 硬删除状态为（0）的项目表
	 * 
	 */
	@Override
	public SGResult hardDeleteItem() {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<Item> list = itemMapper.selectByExample(example);
		for (Item item : list) {
			itemMapper.deleteByPrimaryKey(item.getId());
		}
		return SGResult.build(200, "硬删除项目表成功！");
	}

	/**
	 * 查找item——根据itemId和状态status
	 * 
	 * @param itemId
	 *            要找的项目id
	 * @param status
	 *            状态
	 */
	public Item getItemByItemId(Long itemId, Integer status) {
		// 根据项目id和状态status查询比赛项目信息
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		criteria.andStatusNotEqualTo(0);
		if (status != null)
			// 0-已取消 1-正在报名 2-已结束
			criteria.andStatusEqualTo(status);

		List<Item> list = itemMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return null;
		return list.get(0);
	}

	/**
	 * 查找item列表——根据item详细信息
	 * 
	 * @param 要查找的项目具体信息（game,category,item)
	 */
	@Override
	public List<Item> getItemsByItemDetails(Item item) {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);
		if (!StringUtils.isBlank(item.getGame()))
			criteria.andGameEqualTo(item.getGame());
		if (!StringUtils.isBlank(item.getCategory()))
			criteria.andCategoryEqualTo(item.getCategory());
		if (!StringUtils.isBlank(item.getItem()))
			criteria.andItemEqualTo(item.getItem());
		if (item.getGender() != null && !StringUtils.isBlank(item.getGender().toString()))
			// 设置项目性别查询条件
			criteria.andGenderEqualTo(item.getGender());
		if (item.getStatus() != null)
			criteria.andStatusEqualTo(item.getStatus());

		// 执行查询
		List<Item> items = itemMapper.selectByExample(example);
		
		if (items == null || items.size() <= 0)
			return null;
		return items;

	}

	/**
	 * 查找itemId列表——根据Item具体信息获取ItemId列表
	 * 
	 * @param itemCtr
	 * @return
	 */
	public List<Long> getItemIdsByItemDetails(Item item) {
		List<Item> items = getItemsByItemDetails(item);
		if (items == null || items.size() <= 0)
			return null;
		List<Long> itemIds = new ArrayList<>();
		for (Item item0 : items) {
			itemIds.add(item0.getId());
		}
		
		return itemIds;
	}

	/**
	 * 根据itemId获得赛道数
	 * 
	 * @param itemId
	 *            要查找的项目id
	 */
	@Override
	public Integer getPathNumberByItemId(Long itemId) {
		Item item = itemMapper.selectByPrimaryKey(itemId);
		return item.getPathNum();
	}

	/**
	 * 根据状态status查找项目id
	 * 
	 * @param statuses
	 *            要查询的状态列表
	 */
	@Override
	public List<Long> getItemIdsByStatus(Integer... statuses) {
		ItemExample example = new ItemExample();
		for (Integer status : statuses) {
			Criteria criteria = example.or();
			criteria.andStatusEqualTo(status);
		}
		List<Item> list = itemMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return null;
		List<Long> itemIds = new ArrayList<>();
		for (Item item : list)
			itemIds.add(item.getId());

		return itemIds;
	}

	/**
	 * 维护项目表（将已结束的项目的状态设置为“已结束状态”）
	 * 
	 */
	@Override
	public SGResult maintenanceItem() {
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

		return SGResult.build(200, "维护项目表成功！", list);
	}

	/**
	 * 校级管理员审核某个项目（关闭item）
	 */
	@Override
	public List<Long> reviewByUniversityManager(Item itemToBeChecked) {
		itemToBeChecked.setStatus(1);

		List<Item> items = getItemsByItemDetails(itemToBeChecked);

		List<Long> itemIds = new ArrayList<>();

		for (Item item : items) {
			item.setStatus(2);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
			itemIds.add(item.getId());
		}
		return itemIds;
	}

	
	//未梳理2019.1.14
	/**
	 * 异步查询加载各级名称
	 * 
	 * @param item
	 *            接收的item
	 * @return 根据item返回信息
	 */
	public List<String> getNameByDetailsAndStatus(ItemCtr itemCtr, Integer status) {
		ArrayList<String> result = new ArrayList<>();
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();

		if (!StringUtils.isBlank(itemCtr.getItem()))
			criteria.andItemEqualTo(itemCtr.getItem());
		if (!StringUtils.isBlank(itemCtr.getCategory()))
			criteria.andCategoryEqualTo(itemCtr.getCategory());
		if (!StringUtils.isBlank(itemCtr.getGame()))
			criteria.andGameEqualTo(itemCtr.getGame());
		criteria.andStatusNotEqualTo(0);
		if (status != null)
			criteria.andStatusEqualTo(status);

		List<Item> list = itemMapper.selectByExample(example);

		if (!StringUtils.isBlank(itemCtr.getItem())) {
			String gender;
			for (Item Item : list) {
				gender = genderGroupService.genderIntToStr(Item.getGender());
				if (!result.contains(gender)) {
					result.add(gender);
				}
			}
			return result;
		} else if (!StringUtils.isBlank(itemCtr.getCategory())) {
			String item;
			for (Item Item : list) {
				item = Item.getItem();
				if (!result.contains(item)) {
					result.add(item);
				}
			}
			return result;
		} else if (!StringUtils.isBlank(itemCtr.getGame())) {
			String category;
			for (Item Item : list) {
				category = Item.getCategory();
				if (!result.contains(category)) {
					result.add(category);
				}
			}
			return result;
		} else {
			String game;
			for (Item Item : list) {
				game = Item.getGame();
				if (!result.contains(game)) {
					result.add(game);
				}
			}

			return result;
		}
	}
}
