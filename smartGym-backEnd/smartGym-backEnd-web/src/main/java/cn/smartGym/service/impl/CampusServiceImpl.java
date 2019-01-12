package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.CampuseMapper;
import cn.smartGym.pojo.Campuse;
import cn.smartGym.pojo.CampuseExample;
import cn.smartGym.pojo.CampuseExample.Criteria;
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
	private CampuseMapper CampuseMapper;

	/**
	 * 根据校区id获取校区名
	 */
	public String getCampus(Integer id) {
		if (id == null)
			return null;
		// 根据主键查询
		Campuse Campuse = CampuseMapper.selectByPrimaryKey(id);
		return Campuse.getCampus();
	}

	/**
	 * 根据校区名获取校区id
	 */
	public Integer getId(String campus) {
		if (StringUtils.isBlank(campus))
			return null;

		CampuseExample example = new CampuseExample();
		Criteria criteria = example.createCriteria();
		if (campus != null) {
			criteria.andCampusEqualTo(campus);
		}
		List<Campuse> list = CampuseMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0).getId();
	}

	/**
	 * 获取所有的校区名称
	 */
	public List<String> getAllCampuses() {
		CampuseExample example = new CampuseExample();
		List<Campuse> list = CampuseMapper.selectByExample(example);
		List<String> result = new ArrayList<>();
		if (!list.isEmpty()) {
			for (int n = 0; n < list.size(); n++)
				result.add(list.get(n).getCampus());
		}
		return result;
	}

}
