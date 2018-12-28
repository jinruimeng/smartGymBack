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
	public SGResult addItem(SmartgymItems item) {
		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		// 补全item其他属性
		item.setId(itemId);
		item.setStatus(1); // 0-已取消 1-正在报名 2-已结束
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		smartgymItemsMapper.insert(item);
		// 返回成功
		return SGResult.build(200, "添加比赛项目成功");
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
	public SmartgymItems getItemByItemId(Long itemId) {
		// 根据项目id查询报名项目信息
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		SmartgymItems item = list.get(0);
		return item;
	}

	/**
	 * 异步查询加载比赛项目
	 * 
	 * @param item 接收的item
	 * @return 根据item返回信息
	 */
	public ArrayList<String> applySelect(SmartgymItemsCtr itemCtr) {
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

		return itemCtr;
	}

}
