package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.ItemService;
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
	private CollegeService collegeService;

	@Autowired
	private GenderGroupService genderGroupService;

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemService itemService;

	/**
	 * Controller-Dao层接收bean转换器
	 * 
	 * @param applyCtr
	 *            接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public SmartgymApplications applyCtrtoDao(SmartgymApplicationsCtr applyCtr) {
		// 根据具体项目信息生成itemId
		Long itemId = itemService.getItemIdByItemDetails(applyCtr.getGame(), applyCtr.getCategory(), applyCtr.getItem(),
				genderGroupService.genderStrToInt(applyCtr.getGender()));
		if(itemId == null)
			return null;
		applyCtr.setItemId(itemId);
		// 转换为Dao层的pojo
		SmartgymApplications apply = new SmartgymApplications();
		// 设置学号
		apply.setStudentno(applyCtr.getStudentno());
		// 设置性别
		apply.setGender(genderGroupService.genderStrToInt(applyCtr.getGender()));
		// 设置职位
		apply.setJob(jobService.jobStringToInt(applyCtr.getJob()));
		// 设置项目Id
		apply.setItemId(applyCtr.getItemId());
		// 设置学院
		apply.setCollege(collegeService.getId(applyCtr.getCollege()));

		return apply;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * 
	 * @param apply
	 *            从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public SmartgymApplicationsCtr applyDaotoCtr(SmartgymApplications apply) {
		// 根据itemId获取项目具体信息
		SmartgymItems item = itemService.getItemByItemId(apply.getItemId());
		// 转换为Dao层的pojo
		SmartgymApplicationsCtr applyCtr = new SmartgymApplicationsCtr();
		// 设置项目信息
		applyCtr.setGame(item.getGame());
		applyCtr.setCategory(item.getCategory());
		applyCtr.setItem(item.getItem());
		applyCtr.setItemId(apply.getItemId());
		// 设置用户Id
		applyCtr.setStudentno(apply.getStudentno());
		// 设置职位
		applyCtr.setJob(jobService.jobIntToString(apply.getJob()));
		// 设置性别
		applyCtr.setGender(genderGroupService.genderIntToStr(apply.getGender()));
		// 设置姓名
		applyCtr.setName(apply.getName());
		/*
		 * SmartgymUsersExample userExample = new SmartgymUsersExample();
		 * cn.smartGym.pojo.SmartgymUsersExample.Criteria userCriteria =
		 * userExample.createCriteria();
		 * userCriteria.andStudentnoEqualTo(apply.getStudentno());
		 * applyCtr.setName(smartgymUsersMapper.selectByExample(userExample).get
		 * (0). getName());
		 */
		// 设置学院
		applyCtr.setCollege(collegeService.getCollege(apply.getCollege()));

		return applyCtr;
	}

	/**
	 * 检查是否已报名该项目
	 * 
	 * @param 申请信息
	 * @return 返回信息
	 */
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
	 * 
	 * @param 申请信息
	 * @return 返回给前端的信息 {status, msg, data}
	 */
	@Override
	public SGResult addApply(SmartgymApplications apply) {
		// 数据有效性检验
		if (StringUtils.isBlank(apply.getStudentno()) || StringUtils.isBlank(apply.getJob().toString())
				|| StringUtils.isBlank(apply.getItemId().toString()))
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
		// 0-已删除（或已通过校级审核）1-正在审核 2-院级审核通过
		apply.setCreated(new Date());
		apply.setUpdated(new Date());
		// 插入数据库
		smartgymApplicationsMapper.insert(apply);
		// 返回成功
		return SGResult.build(200, "报名成功", apply);
	}

	/**
	 * 根据学号查询已报名比赛项目列表
	 * 
	 * @param 学号
	 * @return 根据学号查找到的学生已报名比赛项目信息
	 */
	public List<SmartgymApplicationsCtr> getApplycationListByStudentno(String studentno) {
		SmartgymApplicationsExample example = new SmartgymApplicationsExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentnoEqualTo(studentno);
		criteria.andStatusGreaterThanOrEqualTo(1);
		List<SmartgymApplications> list = smartgymApplicationsMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		List<SmartgymApplicationsCtr> result = new ArrayList<>();
		for (SmartgymApplications apply : list) {
			SmartgymApplicationsCtr applyCtr = applyDaotoCtr(apply);
			result.add(applyCtr);
		}
		return result;
	}

}
