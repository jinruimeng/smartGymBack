package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.ItemMapper;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.ItemExample;
import cn.smartGym.pojo.ItemExample.Criteria;
import cn.smartGym.pojoCtr.ItemCtr;
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
	private ItemMapper ItemMapper;

	@Autowired
	private GenderGroupService genderGroupService;

	/**
	 * 添加比赛项目功能
	 * 
	 * @param item 添加的项目
	 * @return 返回给前端的信息
	 */
	public SGResult addItem(ItemCtr itemCtr) {
		// 检查数据合法性
		if (StringUtils.isBlank(itemCtr.getGame()))
			return SGResult.build(200, "赛事不能为空！");
		if (StringUtils.isBlank(itemCtr.getCategory()))
			return SGResult.build(200, "分类不能为空！");
		if (StringUtils.isBlank(itemCtr.getItem()))
			return SGResult.build(200, "项目不能为空！");
		if (StringUtils.isBlank(itemCtr.getGender()))
			return SGResult.build(200, "组别不能为空！");
		if (itemCtr.getDate() == null)
			return SGResult.build(200, "组别不能为空！");
		if (StringUtils.isBlank(itemCtr.getPlace()))
			return SGResult.build(200, "地点不能为空！");

		List<Item> list = getItemsByItemDetails(itemCtr);
		if (!list.isEmpty())
			return SGResult.build(200, "该项目已存在，请先删除！");

		Item item = itemCtrToDao(itemCtr);
		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		// 补全item其他属性
		if (item.getStatus() == null)
			item.setId(itemId);
		item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		ItemMapper.insert(item);
		// 返回成功
		return SGResult.build(200, "添加项目成功!");
	}

	/**
	 * 根据Item具体信息获取ItemId
	 * 
	 * @param itemCtr
	 * @return
	 */
	public List<Long> getItemIdByItemDetails(ItemCtr itemCtr) {
		// 根据项目的名称分类小项等生成比赛项目Id
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);
		if (!StringUtils.isBlank(itemCtr.getGame()))
			criteria.andGameEqualTo(itemCtr.getGame());
		if (!StringUtils.isBlank(itemCtr.getCategory()))
			criteria.andCategoryEqualTo(itemCtr.getCategory());
		if (!StringUtils.isBlank(itemCtr.getItem()))
			criteria.andItemEqualTo(itemCtr.getItem());
		if (!StringUtils.isBlank(itemCtr.getGender()))
			criteria.andGenderEqualTo(genderGroupService.genderStrToInt(itemCtr.getGender()));
		if (itemCtr.getStatus() != null)
			criteria.andStatusEqualTo(itemCtr.getStatus());

		List<Item> items = ItemMapper.selectByExample(example);

		List<Long> itemsId = new ArrayList<>();
		for (Item item : items) {
			itemsId.add(item.getId());
		}

		return itemsId;
	}

	/**
	 * 根据ItemId获取Item实体
	 */
	public ItemCtr getItemByItemId(Long itemId, Integer status) {
		// 根据项目id查询比赛项目信息
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		criteria.andStatusNotEqualTo(0);
		if (status != null)
			criteria.andStatusEqualTo(status);

		List<Item> list = ItemMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		Item item = list.get(0);

		return itemDaoToCtr(item);
	}

	/**
	 * 异步查询加载各级名称
	 * 
	 * @param item 接收的item
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

		List<Item> list = ItemMapper.selectByExample(example);

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

	/**
	 * Controller-Dao层接收bean转换器
	 * 
	 * @param itemCtr 接收前端数据的bean
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
	 * @param item 从数据库中查询出数据封装的bean
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
	 * 根据Item具体信息获取ItemCtr
	 */
	@Override
	public List<ItemCtr> getItemsCtrByItemDetails(ItemCtr itemCtr) {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);

		if (!StringUtils.isBlank(itemCtr.getGame()))
			criteria.andGameEqualTo(itemCtr.getGame());

		if (!StringUtils.isBlank(itemCtr.getCategory()))
			criteria.andCategoryEqualTo(itemCtr.getCategory());

		if (!StringUtils.isBlank(itemCtr.getItem()))
			criteria.andItemEqualTo(itemCtr.getItem());

		if (!StringUtils.isBlank(itemCtr.getGender()))
			// 设置项目性别查询条件
			criteria.andGenderEqualTo(genderGroupService.genderStrToInt(itemCtr.getGender()));

		if (itemCtr.getStatus() != null)
			criteria.andStatusEqualTo(itemCtr.getStatus());

		List<Item> items = ItemMapper.selectByExample(example);

		List<ItemCtr> itemsCtr = new ArrayList<>();

		if (!items.isEmpty()) {
			for (Item item : items) {
				itemsCtr.add(itemDaoToCtr(item));
			}
		}

		return itemsCtr;
	}

	/**
	 * 根据Item具体信息获取ItemCtr
	 */
	@Override
	public List<Item> getItemsByItemDetails(ItemCtr itemCtr) {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);

		if (!StringUtils.isBlank(itemCtr.getGame()))
			criteria.andGameEqualTo(itemCtr.getGame());

		if (!StringUtils.isBlank(itemCtr.getCategory()))
			criteria.andCategoryEqualTo(itemCtr.getCategory());

		if (!StringUtils.isBlank(itemCtr.getItem()))
			criteria.andItemEqualTo(itemCtr.getItem());

		if (!StringUtils.isBlank(itemCtr.getGender()))
			// 设置项目性别查询条件
			criteria.andGenderEqualTo(genderGroupService.genderStrToInt(itemCtr.getGender()));

		if (itemCtr.getStatus() != null)
			criteria.andStatusEqualTo(itemCtr.getStatus());

		List<Item> list = ItemMapper.selectByExample(example);

		return list;
	}

	/**
	 * 删除项目
	 */
	@Override
	public SGResult deleteItem(ItemCtr itemCtr) {
		List<Item> items = getItemsByItemDetails(itemCtr);

		if (items.isEmpty())
			return SGResult.build(200, "没有该项目！");
		else {
			for (Item item : items) {
				item.setStatus(0);
				ItemMapper.updateByPrimaryKeySelective(item);
			}
			return SGResult.build(200, "删除成功！");
		}
	}

	/**
	 * 维护项目表（将已结束的项目的状态设置为“已结束状态”）
	 */
	@Override
	public SGResult maintenanceItem() {
		Date date = new Date();

		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andDateLessThan(date);
		List<Item> list = ItemMapper.selectByExample(example);

		for (Item item : list) {
			item.setStatus(2);
			// 0-已取消，1-正在报名，2-已结束
			item.setUpdated(date);
			ItemMapper.updateByPrimaryKeySelective(item);
		}

		return SGResult.build(200, "维护项目表成功！", list);
	}

	/**
	 * 根据状态获取比赛的id
	 */
	@Override
	public List<Long> selectItemByStatus(Integer... statuses) {
		ItemExample example = new ItemExample();
		for (Integer status : statuses) {
			Criteria criteria = example.or();
			criteria.andStatusEqualTo(status);
		}
		List<Item> list = ItemMapper.selectByExample(example);

		List<Long> itemsId = new ArrayList<>();
		for (Item item : list) {
			itemsId.add(item.getId());
		}

		return itemsId;
	}

	/**
	 * 硬删除状态为（0）的项目表
	 */
	@Override
	public SGResult hardDeleteItem() {
		ItemExample example = new ItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<Item> list = ItemMapper.selectByExample(example);

		for (Item item : list) {
			ItemMapper.deleteByPrimaryKey(item.getId());
		}

		return SGResult.build(200, "硬删除项目表成功！");
	}

}
