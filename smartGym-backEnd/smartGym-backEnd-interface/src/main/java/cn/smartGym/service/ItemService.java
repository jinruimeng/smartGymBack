package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymItems;
import common.utils.SGResult;

/**
 * 比赛项目服务层
 * @author ikangkang
 *
 */
public interface ItemService {

	SGResult addItem(SmartgymItems item);
}
