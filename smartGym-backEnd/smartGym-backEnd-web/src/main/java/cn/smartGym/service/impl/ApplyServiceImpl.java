package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymApplicationsMapper;
import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojo.SmartgymApplicationsExample;
import cn.smartGym.pojo.SmartgymApplicationsExample.Criteria;
import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojo.SmartgymItemsExample;
import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import cn.smartGym.service.ApplyService;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 比赛报名管理Service
 *
 */
@Service
public class ApplyServiceImpl implements ApplyService {

	@Autowired
	private SmartgymApplicationsMapper smartgymApplicationsMapper;

	@Autowired
	private SmartgymItemsMapper smartgymItemsMapper;

	/**
	 * 报名比赛
	 */
	@Override
	public SGResult addApply(SmartgymApplications apply) {
		// 数据有效性检验
		if (apply.getUserId() == null || apply.getJob() == null || apply.getItemId() == null)
			return SGResult.build(401, "报名信息不完整，报名失败");
		SGResult result = checkData(apply);
		if (result.getStatus() != 200) {
			return result;
		}
		// 生成比赛报名id
		final long applyId = IDUtils.genId();
		// 补全apply其他属性
		apply.setId(applyId);
		apply.setStatus(1);
		// 1-正常，0-已删除
		apply.setCreated(new Date());
		apply.setUpdated(new Date());
		// 插入数据库
		smartgymApplicationsMapper.insert(apply);
		// 返回成功
		return SGResult.build(200, "报名成功", apply);
	}

	@Override
	public SGResult checkData(SmartgymApplications apply) {
		// 根据不同的type生成不同的查询条件
		SmartgymApplicationsExample example = new SmartgymApplicationsExample();
		Criteria criteria = example.createCriteria();

		criteria.andJobEqualTo(apply.getJob());
		criteria.andUserIdEqualTo(apply.getUserId());
		criteria.andItemIdEqualTo(apply.getItemId());
		// 执行查询
		List<SmartgymApplications> list = smartgymApplicationsMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (list != null && list.size() > 0) {
			// 如果有数据返回false
			return SGResult.build(400, "已报名该项目");
		}
		// 如果没有数据返回true
		return SGResult.ok();
	}

	@Override
	public SmartgymApplications applyCtrtoDao(SmartgymApplicationsCtr applyCtr) {
		// 设置报名项目Id
		SmartgymItemsExample example = new SmartgymItemsExample();
		cn.smartGym.pojo.SmartgymItemsExample.Criteria criteria = example.createCriteria();
		criteria.andGameEqualTo(applyCtr.getGame());
		criteria.andCategoryEqualTo(applyCtr.getCategoty());
		criteria.andItemEqualTo(applyCtr.getItem());
		if (applyCtr.getGender() == null)
			applyCtr.setGender("男女不限");
		switch (applyCtr.getGender()) {
		case "男女不限":
			criteria.andGenderEqualTo(0);
			break;
		case "男":
			criteria.andGenderEqualTo(1);
			break;
		case "女":
			criteria.andGenderEqualTo(2);
			break;
		case "男女混合":
			criteria.andGenderEqualTo(3);
			break;
		}
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		SmartgymItems itemObject = list.get(0);
		applyCtr.setItemId(itemObject.getId());

		// 转换为Dao层的pojo
		SmartgymApplications applyDao = new SmartgymApplications();
		// 设置用户Id
		applyDao.setUserId(applyCtr.getUserId());
		// 设置职位
		switch (applyCtr.getJob()) {
		case "队员":
			applyDao.setJob(0);
			break;
		case "领队":
			applyDao.setJob(1);
			break;
		case "教练":
			applyDao.setJob(2);
			break;
		case "联系人员":
			applyDao.setJob(3);
			break;
		case "工作人员":
			applyDao.setJob(4);
			break;
		default:
			applyDao.setJob(0);
		}
		// 设置项目Id
		applyDao.setItemId(applyCtr.getItemId());

		return applyDao;
	}

	@Override
	public SmartgymApplicationsCtr applyDaotoCtr(SmartgymApplications applyDao) {
		// 查询报名项目信息
		SmartgymItemsExample example = new SmartgymItemsExample();
		cn.smartGym.pojo.SmartgymItemsExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(applyDao.getItemId());
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		SmartgymItems itemObject = list.get(0);
		
		// 转换为Dao层的pojo
		SmartgymApplicationsCtr applyCtr = new SmartgymApplicationsCtr();
		// 设置项目信息
		applyCtr.setGame(itemObject.getGame());
		applyCtr.setCategoty(itemObject.getCategory());
		applyCtr.setItem(itemObject.getItem());
		applyCtr.setItemId(applyDao.getItemId());;
		// 设置用户Id
		applyCtr.setUserId(applyDao.getUserId());
		// 设置职位
		switch (applyDao.getJob()) {
		case 0:
			applyCtr.setJob("队员");
			break;
		case 1:
			applyCtr.setJob("领队");
			break;
		case 2:
			applyCtr.setJob("教练");
			break;
		case 3:
			applyCtr.setJob("联系人员");
			break;
		case 4:
			applyCtr.setJob("工作人员");
			break;
		default:
			applyCtr.setJob("队员");
		}

		return applyCtr;
	}

}
