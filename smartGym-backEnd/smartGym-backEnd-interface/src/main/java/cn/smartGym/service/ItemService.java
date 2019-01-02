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

	List<SmartgymItems> checkItem(SmartgymItemsCtr itemCtr);

	List<String> applySelect(SmartgymItemsCtr itemCtr);

	List<Long> selectItemByStatus(Integer... statuses);

	SGResult addItem(SmartgymItemsCtr itemCtr);

	SGResult deleteItem(SmartgymItemsCtr itemCtr);
	
	SGResult hardDeleteItem();

	SGResult maintenanceItem();

	List<Long> getItemIdByItemDetails(SmartgymItemsCtr itemCtr);

	SmartgymItemsCtr getItemByItemId(Long itemId);

	List<SmartgymItemsCtr> getItemsByGame(String game);
	
}
