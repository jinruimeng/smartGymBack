package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymCampusesMapper;
import cn.smartGym.pojo.SmartgymCampuses;
import cn.smartGym.pojo.SmartgymCampusesExample;
import cn.smartGym.pojo.SmartgymCampusesExample.Criteria;
import cn.smartGym.service.CampusService;

/**
 * 校区管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class CampusServiceImpl implements CampusService {

	@Autowired
	private SmartgymCampusesMapper smartgymCampusesMapper;

	/**
	 * 根据校区id获取校区名
	 */
	public String getCampus(Integer id) {

		// 根据主键查询
		SmartgymCampuses smartgymCampuses = smartgymCampusesMapper.selectByPrimaryKey(id);
		return smartgymCampuses.getCampus();
	}

	/**
	 * 根据校区名获取校区id
	 */
	public Integer getId(String campus) {
		SmartgymCampusesExample example = new SmartgymCampusesExample();
		Criteria criteria = example.createCriteria();
		criteria.andCampusEqualTo(campus);
		List<SmartgymCampuses> list = smartgymCampusesMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

	/**
	 * 获取所有的校区名称
	 */
	@Override
	public List<String> getAllCampuses() {
		SmartgymCampusesExample example = new SmartgymCampusesExample();
		List<SmartgymCampuses> list = smartgymCampusesMapper.selectByExample(example);
		List<String> result = new ArrayList<>();
		if(!list.isEmpty()) {
			for(int n = 0;n<list.size();n++)
				result.add(list.get(n).getCampus());
		}
		return result;
	}

}
