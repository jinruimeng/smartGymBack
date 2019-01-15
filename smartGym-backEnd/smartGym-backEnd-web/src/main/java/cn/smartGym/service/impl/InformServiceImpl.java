package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.InformationMapper;
import cn.smartGym.pojo.Information;
import cn.smartGym.pojo.InformationExample;
import cn.smartGym.pojo.InformationExample.Criteria;
import cn.smartGym.service.InformService;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 通知/活动管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class InformServiceImpl implements InformService {

	@Autowired
	private InformationMapper InformationMapper;

	@Override
	/**
	 * 新增通知
	 */
	public SGResult addInform(Information Information) {
		Information.setId(IDUtils.genId());
		Information.setStatus(1);// 0删除，1正常
		Information.setCreated(new Date());
		Information.setUpdated(new Date());
		InformationMapper.insert(Information);

		return SGResult.build(200, "新增通知成功！");
	}

	@Override
	/**
	 * 更新通知
	 */
	public SGResult updateInform(Information Information) {

		InformationMapper.updateByPrimaryKeySelective(Information);
		return SGResult.build(200, "更新通知成功！");
	}

	@Override
	/**
	 * 根据id删除通知
	 */
	public SGResult deleteInformById(Long id) {
		InformationMapper.deleteByPrimaryKey(id);
		return SGResult.build(200, "删除通知成功！");
	}

	@Override
	/**
	 * 根据通知类型返回通知列表：0-通知+活动，1-通知，2-活动 注意：不把通知正文（description)和备注（remark）返回
	 */
	public SGResult getInformList(Integer type) {
		// 根据type查询inform表
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		InformationExample example = new InformationExample();
		Criteria criteria = example.createCriteria();
		if (type == 0)
			criteria.andTypeGreaterThan(type);
		else
			criteria.andTypeEqualTo(type);
		criteria.andStatusEqualTo(1);
		List<Information> list = InformationMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return SGResult.build(404, "未找到通知！");
		for (Information Information : list) {
			Information.setDescription("");
			Information.setRemark("");
		}
		return SGResult.build(200, "返回通知列表成功！", list);
	}

	@Override
	/**
	 * 根据通知id返回通知具体信息
	 */
	public SGResult getInformById(Long id) {
		Information Information = InformationMapper.selectByPrimaryKey(id);
		if (Information == null)
			return SGResult.build(404, "没有找到您查询的通知信息！");
		return SGResult.build(200, "查找通知成功！", Information);
	}

}
