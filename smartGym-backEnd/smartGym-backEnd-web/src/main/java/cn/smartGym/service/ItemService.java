package cn.smartGym.service;

import java.util.List;
import java.util.Set;

import cn.smartGym.pojo.Item;
import common.utils.SGResult;

/**
 * 比赛项目服务层
 * 
 * @author ikangkang
 *
 */
public interface ItemService {

	SGResult addItem(Item item);

	SGResult addItemExcludesPlayers(Item item);

	void hardDeleteItem();

	SGResult deleteItem(Item itemToBeDeleted);

	void maintenanceItem();

	List<Long> reviewByUniversityManager(Item itemToBeChecked);

	Integer getPathNumberByItemId(Long itemId);

	Item getItemByItemIdAndStatuses(Long itemId, Integer... statuses);

	List<Item> getItemsByDetailsAndStatuses(Item item, Integer... statuses);

	List<Long> getItemIdsByItems(List<Item> items);

	Set<String> getPropertiesByItems(List<Item> items, String property);
}
