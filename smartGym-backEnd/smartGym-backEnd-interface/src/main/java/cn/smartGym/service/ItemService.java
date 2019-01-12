package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.Item;
import cn.smartGym.pojoCtr.ItemCtr;
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

	List<String> getNameByDetailsAndStatus(ItemCtr itemCtr, Integer status);

	List<Long> selectItemByStatus(Integer... statuses);

	SGResult addItem(ItemCtr itemCtr);

	SGResult deleteItem(ItemCtr itemCtr);

	SGResult hardDeleteItem();

	SGResult maintenanceItem();

	List<Long> getItemIdByItemDetails(ItemCtr itemCtr);

	ItemCtr getItemByItemId(Long itemId, Integer status);

	List<ItemCtr> getItemsCtrByItemDetails(ItemCtr itemCtr);

	List<Item> getItemsByItemDetails(ItemCtr itemCtr);

}
