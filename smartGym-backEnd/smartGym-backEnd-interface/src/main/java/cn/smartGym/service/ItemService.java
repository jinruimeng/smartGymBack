package cn.smartGym.service;

import java.util.ArrayList;

import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import common.utils.SGResult;

/**
 * 比赛项目服务层
 * 
 * @author ikangkang
 *
 */
public interface ItemService {

	SmartgymItems itemCtrToDao(SmartgymItemsCtr itemCtr);

	SmartgymItemsCtr itemDaoToCtr(SmartgymItems item);

	ArrayList<String> applySelect(SmartgymItemsCtr itemCtr);

	SGResult addItem(SmartgymItems item);
	
	Long getItemIdByItemDetails(String game, String category, String item, Integer gender);
	
	SmartgymItems getItemByItemId(Long itemId);
	
}
