package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymInformMapper;
import cn.smartGym.pojo.SmartgymInform;
import cn.smartGym.pojo.SmartgymInformExample;
import cn.smartGym.pojo.SmartgymInformExample.Criteria;
import cn.smartGym.service.InformService;
import common.utils.IDUtils;
import common.utils.SGResult;
/**
 * 通知/活动管理服务层
 * @author Ruimeng Jin
 *
 */
@Service
public class InformServiceImpl implements InformService {

	@Autowired
	private SmartgymInformMapper smartgymInformMapper;
	
	@Override
	/**
	 * 新增通知
	 */
	public SGResult addInform(SmartgymInform smartgymInform) {
		smartgymInform.setId(IDUtils.genId());
		smartgymInform.setStatus(1);//0删除，1正常
		smartgymInform.setCreated(new Date());
		smartgymInform.setUpdated(new Date());
		smartgymInformMapper.insert(smartgymInform);
	
		return SGResult.build(200, "新增通知成功！");
	}

	@Override
	/**
	 * 更新通知
	 */
	public SGResult updateInform(SmartgymInform smartgymInform) {
		
		smartgymInformMapper.updateByPrimaryKeySelective(smartgymInform);
		return SGResult.build(200, "更新通知成功！");
	}

	@Override
	/**
	 * 根据id删除通知
	 */
	public SGResult deleteInformById(Long id) {
		smartgymInformMapper.deleteByPrimaryKey(id);
		return SGResult.build(200, "删除通知成功！");
	}
	
	
	
	@Override
	/**
	 * 根据通知类型返回通知列表：0-通知+活动，1-通知，2-活动
	 * 注意：不把通知正文（description)和备注（remark）返回
	 */
	public SGResult getInformList(Integer type) {
		// 根据type查询inform表
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		SmartgymInformExample example = new SmartgymInformExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andStatusEqualTo(1);
		List<SmartgymInform> list = smartgymInformMapper.selectByExample(example);
		if(list == null || list.size() == 0)
			return SGResult.build(404, "返回通知列表失败！");
		for (SmartgymInform smartgymInform : list) {
			smartgymInform.setDescription("");
			smartgymInform.setRemark("");
		}
		return SGResult.build(200, "返回通知列表成功！", list);
	}

	@Override
	/**
	 * 根据通知id返回通知具体信息
	 */
	public SGResult getInformById(Long id) {
		SmartgymInform smartgymInform = smartgymInformMapper.selectByPrimaryKey(id);
		if(smartgymInform == null)
			return SGResult.build(404, "没有找到您要找的通知！");
		return SGResult.build(200, "查找通知成功！");
	}

	

}
