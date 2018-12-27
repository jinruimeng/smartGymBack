package cn.smartGym.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymCollegesMapper;
import cn.smartGym.pojo.SmartgymColleges;
import cn.smartGym.pojo.SmartgymCollegesExample;
import cn.smartGym.pojo.SmartgymCollegesExample.Criteria;
import cn.smartGym.service.CollegeService;

/**
 * 学院管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class CollegeServiceImpl implements CollegeService {

	@Autowired
	private SmartgymCollegesMapper smartgymCollegesMapper;

	/**
	 * 根据学院id获取学院名称
	 */
	@Override
	public String getCollege(Integer id) {
		// 根据主键查询
		SmartgymColleges smartgymColleges = smartgymCollegesMapper.selectByPrimaryKey(id);
		return smartgymColleges.getCollege();
	}

	/**
	 * 根据学院名称获取学院id
	 */
	@Override
	public Integer getId(String college) {
		SmartgymCollegesExample example = new SmartgymCollegesExample();
		Criteria criteria = example.createCriteria();
		criteria.andCollegeEqualTo(college);
		List<SmartgymColleges> list = smartgymCollegesMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

}
