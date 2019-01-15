package cn.smartGym.service;

import java.util.List;

import cn.smartGym.controller.pojoCtr.ItemCtr;
import cn.smartGym.pojo.Item;
import common.utils.SGResult;

/**
 * 比赛项目服务层
 * 
 * @author ikangkang
 *
 */
public interface ItemService {

	Item itemCtrToDao(ItemCtr itemCtr);

	ItemCtr itemDaoToCtr(Item item);
	
	SGResult addItem(Item item);

	SGResult deleteItem(Item itemToBeDeleted);
	
	SGResult hardDeleteItem();
	
	Item getItemByItemId(Long itemId, Integer status);
	
	List<Item> getItemsByItemDetails(Item item);
	
	List<Long> getItemIdsByItemDetails(Item item);
	
	Integer getPathNumberByItemId(Long itemId);
	
	List<Long> getItemIdsByStatus(Integer... statuses);
	
	SGResult maintenanceItem();
	
	List<Long> reviewByUniversityManager(Item itemToBeChecked);

	//
	List<String> getNameByDetailsAndStatus(ItemCtr itemCtr, Integer status);

}
