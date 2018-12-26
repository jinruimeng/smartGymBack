package cn.smartGym.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.service.ItemService;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 比赛项目管理Service
 * @author ikangkang
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private SmartgymItemsMapper smartgymItemsMapper;
	
	/**
	 * 添加比赛项目
	 */
	public SGResult addItem(SmartgymItems item) {
		// 生成比赛项目id
		final long itemId = IDUtils.genId();
		//补全item其他属性
		item.setId(itemId);
		item.setStatus(1); //0-删除  1-正常
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//插入数据库
		smartgymItemsMapper.insert(item);
		//返回成功
		return SGResult.build(200, "添加比赛项目成功");
	}
	
	/**
	 * Controller-Dao层接收bean转换器
	 * @param itemCtr 接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	public SmartgymItems itemCtrToDao(SmartgymItemsCtr itemCtr) {
		SmartgymItems item = new SmartgymItems();
		item.setGame(itemCtr.getGame());
		item.setCategory(itemCtr.getCategory());
		item.setItem(itemCtr.getItem());
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
			item.setGender(3);
		}
		item.setDescription(itemCtr.getDescription());
		return item;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * @param item 从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	public SmartgymItemsCtr itemDaoToCtr(SmartgymItems item) {
		SmartgymItemsCtr itemCtr = new SmartgymItemsCtr();
		itemCtr.setGame(item.getGame());
		itemCtr.setCategory(item.getCategory());
		itemCtr.setItem(item.getItem());
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
			itemCtr.setGender("未确定");
		}
		itemCtr.setDescription(item.getDescription());
		return itemCtr;
	}

}
