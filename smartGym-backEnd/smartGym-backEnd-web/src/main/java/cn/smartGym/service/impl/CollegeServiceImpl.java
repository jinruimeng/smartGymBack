package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.CollegeMapper;
import cn.smartGym.pojo.College;
import cn.smartGym.pojo.CollegeExample;
import cn.smartGym.pojo.CollegeExample.Criteria;
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
	private CollegeMapper CollegeMapper;

	/**
	 * 根据学院id获取学院名称
	 */
	@Override
	public String getCollege(Integer id) {
		if (id == null)
			return null;
		// 根据主键查询
		College College = CollegeMapper.selectByPrimaryKey(id);
		return College.getCollege();
	}

	/**
	 * 根据学院名称获取学院id
	 */
	@Override
	public Integer getId(String college) {
		if(StringUtils.isBlank(college))
			return null;
		CollegeExample example = new CollegeExample();
		Criteria criteria = example.createCriteria();
		criteria.andCollegeEqualTo(college);
		List<College> list = CollegeMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

	/**
	 * 获取所有的学院名称
	 */
	@Override
	public List<String> getAllColleges() {
		CollegeExample example = new CollegeExample();
		List<College> list = CollegeMapper.selectByExample(example);
		List<String> result = new ArrayList<>();
		if (!list.isEmpty()) {
			for (int n = 0; n < list.size(); n++)
				result.add(list.get(n).getCollege());
		}
		return result;
	}

	/**
	 * 获取所有的学院id及名称
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, String> getAllCollegeIdsAndName() {
		CollegeExample example = new CollegeExample();
		List<College> colleges = CollegeMapper.selectByExample(example);
		Map<Integer, String> result = new HashedMap();
		for (College college : colleges) {
			result.put(college.getId(), college.getCollege());
		}
		return result;
	}
}
