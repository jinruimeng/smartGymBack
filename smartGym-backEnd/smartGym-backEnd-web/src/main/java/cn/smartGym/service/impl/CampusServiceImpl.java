package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.CampusMapper;
import cn.smartGym.pojo.Campus;
import cn.smartGym.pojo.CampusExample;
import cn.smartGym.pojo.CampusExample.Criteria;
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
	private CampusMapper campusMapper;

	/**
	 * 根据校区id获取校区名
	 */
	public String getCampus(Integer id) {
		if (id == null)
			return null;
		// 根据主键查询
		Campus Campus = campusMapper.selectByPrimaryKey(id);
		return Campus.getCampus();
	}

	/**
	 * 根据校区名获取校区id
	 */
	public Integer getId(String campus) {
		if (StringUtils.isBlank(campus))
			return null;

		CampusExample example = new CampusExample();
		Criteria criteria = example.createCriteria();
		if (campus != null) {
			criteria.andCampusEqualTo(campus);
		}
		List<Campus> list = campusMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

	/**
	 * 获取所有的校区名称
	 */
	public List<String> getAllCampuses() {
		CampusExample example = new CampusExample();
		List<Campus> list = campusMapper.selectByExample(example);
		List<String> result = new ArrayList<>();
		if (!list.isEmpty()) {
			for (int n = 0; n < list.size(); n++)
				result.add(list.get(n).getCampus());
		}
		return result;
	}

}
