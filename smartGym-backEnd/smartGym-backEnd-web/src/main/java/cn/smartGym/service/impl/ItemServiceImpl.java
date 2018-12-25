package cn.smartGym.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.pojo.SmartgymItems;
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

}
