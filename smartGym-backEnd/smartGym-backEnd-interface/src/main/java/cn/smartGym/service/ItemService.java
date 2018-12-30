package cn.smartGym.service;

import java.util.List;

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

	List<String> applySelect(SmartgymItemsCtr itemCtr);

	SGResult addItem(SmartgymItemsCtr itemCtr);
	
	Long getItemIdByItemDetails(String game, String category, String item, Integer gender);
	
	SmartgymItemsCtr getItemByItemId(Long itemId);
	
	List<SmartgymItemsCtr> getItemsByGame(String game);
}
