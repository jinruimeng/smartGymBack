package cn.smartGym.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.InformationMapper;
import cn.smartGym.pojo.Information;
import cn.smartGym.pojo.InformationExample;
import cn.smartGym.pojo.InformationExample.Criteria;
import cn.smartGym.service.InformService;
import common.enums.ErrorCode;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 通知/活动管理服务层
 * 
 * @author ikangkang
 *
 */
@Service
public class InformServiceImpl implements InformService {

	@Autowired
	private InformationMapper informationMapper;

	/**
	 * 新增通知
	 */
	@Override
	public void addInform(Information Information) {
		Information.setId(IDUtils.genId());
		Information.setStatus(1);// 0删除，1正常
		Information.setCreated(new Date());
		Information.setUpdated(new Date());
		informationMapper.insert(Information);
	}

	/**
	 * 硬删除
	 */
	@Override
	public void hardDeleteInformation() {
		InformationExample example = new InformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		informationMapper.deleteByExample(example);
	}

	/**
	 * 根据id删除通知
	 */
	@Override
	public SGResult deleteInformById(Long... ids) {
		// 设置查询条件
		InformationExample example = new InformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andIdIn(Arrays.asList(ids));

		// 设置模板
		Information information = new Information();
		information.setStatus(0);
		information.setUpdated(new Date());
		informationMapper.updateByExampleSelective(information, example);

		return SGResult.ok("删除通知成功！");
	}

	/**
	 * 更新通知
	 */
	@Override
	public void updateInform(Information Information) {

		informationMapper.updateByPrimaryKeySelective(Information);
	}

	@Override
	/**
	 * 根据通知类型返回通知列表：0-通知+活动，1-通知，2-活动
	 * 注意：不把通知正文（description)和备注（remark）返回
	 */
	public SGResult getInformList(Integer type) {
		InformationExample example = new InformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andStatusEqualTo(1);
		List<Information> list = informationMapper.selectByExample(example);
		if(list == null || list.size() == 0)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "返回通知列表失败！");
		for (Information inform : list) {
			inform.setDescription("");
			inform.setRemark("");
		}
		return SGResult.build(200, "返回通知列表成功！", list);
	}

	@Override
	/**
	 * 根据通知id返回通知具体信息
	 */
	public SGResult getInformById(Long id) {
		Information inform = informationMapper.selectByPrimaryKey(id);
		if(inform == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "没有找到您要找的通知！");
		return SGResult.build(200, "查找通知成功！");
	}
}
