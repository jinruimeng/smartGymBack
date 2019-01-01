package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojo.SmartgymItemsExample;
import cn.smartGym.pojo.SmartgymItemsExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
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
	private SmartgymItemsMapper smartgymItemsMapper;

	@Autowired
	private GenderGroupService genderGroupService;

	/**
	 * 添加比赛项目功能
	 * 
	 * @param item 添加的项目
	 * @return 返回给前端的信息
	 */
	public SGResult addItem(SmartgymItemsCtr itemCtr) {
		List<SmartgymItems> list = checkItem(itemCtr);
		if (!list.isEmpty())
			return SGResult.build(200, "该项目已存在，请先删除！");

		SmartgymItems item = itemCtrToDao(itemCtr);
		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		// 补全item其他属性
		if (item.getStatus() == null)
			item.setId(itemId);
		item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		smartgymItemsMapper.insert(item);
		// 返回成功
		return SGResult.build(200, "添加项目成功!");
	}

	/**
	 * 根据Item具体信息获取ItemId
	 * 
	 * @param playerCtr
	 * @return
	 */
	public Long getItemIdByItemDetails(String game, String category, String item, Integer gender) {
		// 根据项目的名称分类小项等生成比赛项目Id
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGameEqualTo(game);
		criteria.andCategoryEqualTo(category);
		criteria.andItemEqualTo(item);
		// 设置项目性别查询条件
		criteria.andGenderEqualTo(gender);

		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

	/**
	 * 根据ItemId获取Item实体
	 */
	public SmartgymItemsCtr getItemByItemId(Long itemId) {
		// 根据项目id查询报名项目信息
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		SmartgymItems item = list.get(0);
		return itemDaoToCtr(item);
	}

	/**
	 * 异步查询加载比赛项目
	 * 
	 * @param item 接收的item
	 * @return 根据item返回信息
	 */
	public List<String> applySelect(SmartgymItemsCtr itemCtr) {
		ArrayList<String> result = new ArrayList<>();
		List<SmartgymItems> list = new ArrayList<>();
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();

		if (!StringUtils.isBlank(itemCtr.getGame())) {
			criteria.andGameEqualTo(itemCtr.getGame());
		} else if (!StringUtils.isBlank(itemCtr.getCategory())) {
			criteria.andCategoryEqualTo(itemCtr.getCategory());
		} else if (!StringUtils.isBlank(itemCtr.getItem())) {
			criteria.andItemEqualTo(itemCtr.getItem());
		} else {
		}
		criteria.andDateGreaterThan(new Date());
		criteria.andStatusEqualTo(1);

		list = smartgymItemsMapper.selectByExample(example);
		if (!StringUtils.isBlank(itemCtr.getItem())) {
			String gender;
			for (SmartgymItems smartgymItems : list) {
				gender = genderGroupService.genderIntToStr(smartgymItems.getGender());
				if (!result.contains(gender)) {
					result.add(gender);
				}
			}
			return result;
		} else if (!StringUtils.isBlank(itemCtr.getCategory())) {
			String item;
			for (SmartgymItems smartgymItems : list) {
				item = smartgymItems.getItem();
				if (!result.contains(item)) {
					result.add(item);
				}
			}
			return result;
		} else if (!StringUtils.isBlank(itemCtr.getGame())) {
			String category;
			for (SmartgymItems smartgymItems : list) {
				category = smartgymItems.getCategory();
				if (!result.contains(category)) {
					result.add(category);
				}
			}
			return result;
		} else {
			String game;
			for (SmartgymItems smartgymItems : list) {
				game = smartgymItems.getGame();
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
	public SmartgymItems itemCtrToDao(SmartgymItemsCtr itemCtr) {
		SmartgymItems item = new SmartgymItems();
		item.setGame(itemCtr.getGame());
		item.setCategory(itemCtr.getCategory());
		item.setItem(itemCtr.getItem());
		item.setDate(itemCtr.getDate());
		item.setPlace(itemCtr.getPlace());
		item.setParticipantNum(itemCtr.getParticipantNum());
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
	public SmartgymItemsCtr itemDaoToCtr(SmartgymItems item) {
		SmartgymItemsCtr itemCtr = new SmartgymItemsCtr();
		itemCtr.setGame(item.getGame());
		itemCtr.setCategory(item.getCategory());
		itemCtr.setItem(item.getItem());
		itemCtr.setDate(item.getDate());
		itemCtr.setPlace(item.getPlace());
		itemCtr.setParticipantNum(item.getParticipantNum());
		itemCtr.setDescription(item.getDescription());
		itemCtr.setGender(genderGroupService.genderIntToStr(item.getGender()));
		if (item.getId() != null)
			itemCtr.setId(item.getId());
		if (item.getStatus() != null)
			itemCtr.setStatus(item.getStatus());

		return itemCtr;
	}

	/**
	 * 获取Game下的所有Items
	 */
	@Override
	public List<SmartgymItemsCtr> getItemsByGame(String game) {
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGameEqualTo(game);
		criteria.andStatusGreaterThanOrEqualTo(1);
		example.setOrderByClause("category");
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		List<SmartgymItemsCtr> result = new ArrayList<>();
		for (SmartgymItems smartgymItems : list) {
			result.add(itemDaoToCtr(smartgymItems));
		}
		return result;
	}

	/**
	 * 检查项目是否已存在
	 */
	@Override
	public List<SmartgymItems> checkItem(SmartgymItemsCtr itemCtr) {
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGameEqualTo(itemCtr.getGame());
		criteria.andCategoryEqualTo(itemCtr.getCategory());
		criteria.andCategoryEqualTo(itemCtr.getItem());
		criteria.andGenderEqualTo(genderGroupService.genderStrToInt(itemCtr.getGender()));
		criteria.andStatusGreaterThanOrEqualTo(1);

		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);

		return list;
	}

	/**
	 * 删除项目
	 */
	@Override
	public SGResult deleteItem(SmartgymItemsCtr itemCtr) {
		List<SmartgymItems> list = checkItem(itemCtr);

		if (list.size() == 0)
			return SGResult.build(200, "没有该项目！");
		else {
			SmartgymItems item = list.get(0);
			item.setStatus(0);
			smartgymItemsMapper.updateByPrimaryKeySelective(item);
			return SGResult.build(200, "删除成功！");
		}
	}

	/**
	 * 维护项目表（将已结束的项目的状态设置为“已结束状态”）
	 */
	@Override
	public SGResult maintenanceItem() {
		Date date = new Date();

		System.out.println(date);

		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andDateLessThan(date);
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);

		for (SmartgymItems item : list) {
			item.setStatus(2);
			// 0-已取消，1-正在报名，2-已结束
			item.setUpdated(date);
			smartgymItemsMapper.updateByPrimaryKeySelective(item);
		}

		return SGResult.build(200, "维护项目表成功！", list);
	}

	/**
	 * 根据状态获取比赛的id
	 */
	@Override
	public List<Long> selectItemByStatus(Integer... statuses) {
		SmartgymItemsExample example = new SmartgymItemsExample();
		for (Integer status : statuses) {
			Criteria criteria = example.or();
			criteria.andStatusEqualTo(status);
		}
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);

		List<Long> itemsId = new ArrayList<>();
		for (SmartgymItems item : list) {
			itemsId.add(item.getId());
		}

		return itemsId;
	}

	/**
	 * 硬删除状态为（0）的项目表
	 */
	@Override
	public SGResult hardDeleteItem() {
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);

		for (SmartgymItems item : list) {
			smartgymItemsMapper.deleteByPrimaryKey(item.getId());
		}

		return SGResult.build(200, "硬删除项目表成功！");
	}

}
