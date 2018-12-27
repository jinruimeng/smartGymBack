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
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.JobService;
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
	
	@Autowired
	private GenderGroupService genderGroupService;
	
	@Autowired
	private JobService jobService;

	@Override
	public SmartgymApplications applyCtrtoDao(SmartgymApplicationsCtr applyCtr) {
		// 设置报名项目Id
		SmartgymItemsExample example = new SmartgymItemsExample();
		cn.smartGym.pojo.SmartgymItemsExample.Criteria criteria = example.createCriteria();
		criteria.andGameEqualTo(applyCtr.getGame());
		criteria.andCategoryEqualTo(applyCtr.getCategory());
		criteria.andItemEqualTo(applyCtr.getItem());
		// 设置性别
		criteria.andGenderEqualTo(genderGroupService.genderStrToInt(applyCtr.getGender()));
		
		List<SmartgymItems> list = smartgymItemsMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		SmartgymItems itemObject = list.get(0);
		applyCtr.setItemId(itemObject.getId());

		// 转换为Dao层的pojo
		SmartgymApplications applyDao = new SmartgymApplications();
		// 设置用户Id
		applyDao.setStudentno(applyCtr.getStudentno());
		// 设置职位
		applyDao.setJob(jobService.jobStringToInt(applyCtr.getJob()));
		// 设置项目itemId
		applyDao.setItemId(applyCtr.getItemId());
		// 设置性别
		applyDao.setGender(genderGroupService.genderStrToInt(applyCtr.getGender()));
		
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
		applyCtr.setCategory(itemObject.getCategory());
		applyCtr.setItem(itemObject.getItem());
		applyCtr.setItemId(applyDao.getItemId());
		// 设置用户Id
		applyCtr.setStudentno(applyDao.getStudentno());
		// 设置职位
		applyCtr.setJob(jobService.jobIntToString(applyDao.getJob()));
		// 设置性别
		applyCtr.setGender(genderGroupService.genderIntToStr(applyDao.getGender()));
		
		return applyCtr;
	}
	
	@Override
	public SGResult checkData(SmartgymApplications apply) {
		SmartgymApplicationsExample example = new SmartgymApplicationsExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andJobEqualTo(apply.getJob());
		criteria.andStudentnoEqualTo(apply.getStudentno());
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
	
	/**
	 * 报名比赛
	 */
	@Override
	public SGResult addApply(SmartgymApplications apply) {
		// 数据有效性检验
		if (apply.getStudentno() == null || apply.getJob() == null || apply.getItemId() == null)
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
}
