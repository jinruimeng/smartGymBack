package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojo.SmartgymItemsExample;
import cn.smartGym.pojo.SmartgymItemsExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
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
		item.setParticipantnums(itemCtr.getParticipantnums());
		item.setDescription(itemCtr.getDescription());
		switch (itemCtr.getGender()) {
		case "男子组":
			item.setGender(0);
			break;
		case "女子组":
			item.setGender(1);
			break;
		case "男女混合":
			item.setGender(2);
			break;
		default:
			item.setGender(2);
		}
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
		itemCtr.setParticipantnums(item.getParticipantnums());
		itemCtr.setDescription(item.getDescription());
		switch (item.getGender()) {
		case 0:
			itemCtr.setGender("男子组");
			break;
		case 1:
			itemCtr.setGender("女子组");
			break;
		case 2:
			itemCtr.setGender("男女混合");
			break;
		default:
			itemCtr.setGender("男女混合");
		}
		return itemCtr;
	}
	
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
		item.setStatus(1); // 0-删除 1-正常
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入数据库
		smartgymItemsMapper.insert(item);
		// 返回成功
		return SGResult.build(200, "添加比赛项目成功");
	}

	
	/**
	 * 显示比赛项目列表功能
	 * 
	 * @param item 添加的项目
	 * @return 返回给前端的信息
	 */
	public ArrayList<String> select(SmartgymItemsCtr itemCtr) {
		ArrayList<String> result = new ArrayList<>();
		List<SmartgymItems> list = new ArrayList<>();
		SmartgymItemsExample example = new SmartgymItemsExample();
		Criteria criteria = example.createCriteria();

		if (itemCtr.getGame() != null) {
			criteria.andGameEqualTo(itemCtr.getGame());
		} else if (itemCtr.getCategory() != null) {
			criteria.andCategoryEqualTo(itemCtr.getCategory());
		} else if (itemCtr.getItem() != null) {
			criteria.andItemEqualTo(itemCtr.getItem());
		} else {
		}
		list = smartgymItemsMapper.selectByExample(example);
		if (itemCtr.getItem() != null) {
			String gender;
			for (int i = 0; i < list.size(); i++) {
				switch (list.get(i).getGender()) {
				case 0:
					gender = "男子组";
					break;
				case 1:
					gender = "女子组";
					break;
				case 2:
					gender = "男女混合";
					break;
				default:
					gender = "男女混合";
				}
				if (!result.contains(gender)) {
					result.add(gender);
				}
			}
			return result;
		} else if (itemCtr.getCategory() != null) {
			for (int i = 0; i < list.size(); i++) {
				if (!result.contains(list.get(i).getItem())) {
					result.add(list.get(i).getItem());
				}
			}
			return result;
		} else if (itemCtr.getGame() != null) {
			for (int i = 0; i < list.size(); i++) {
				if (!result.contains(list.get(i).getCategory())) {
					result.add(list.get(i).getCategory());
				}
			}
			return result;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (!result.contains(list.get(i).getGame())) {
					result.add(list.get(i).getGame());
				}
			}
			return result;
		}
	}

}
