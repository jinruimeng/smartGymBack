 package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.ApplicationMapper;
import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.ApplicationExample;
import cn.smartGym.pojo.ApplicationExample.Criteria;
import cn.smartGym.pojoCtr.ApplicationCtr;
import cn.smartGym.pojoCtr.ItemCtr;
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
	private ApplicationMapper ApplicationMapper;

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
	 * @param applyCtr 接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public Application applyCtrToDao(ApplicationCtr applyCtr) {
		ItemCtr itemsCtr = new ItemCtr();
		itemsCtr.setGame(applyCtr.getGame());
		itemsCtr.setCategory(applyCtr.getCategory());
		itemsCtr.setItem(applyCtr.getItem());
		itemsCtr.setGender(applyCtr.getGender());
		// 0-已删除 1-正在报名 2-已结束
		itemsCtr.setStatus(1);

		// 根据具体项目信息查找itemId
		List<Long> list = itemService.getItemIdByItemDetails(itemsCtr);

		if (list == null || list.isEmpty())
			return null;

		applyCtr.setItemId(list.get(0));

		// 转换为Dao层的pojo
		Application apply = new Application();
		// 设置姓名
		apply.setName(applyCtr.getName());
		// 设置学院
		apply.setCollege(collegeService.getId(applyCtr.getCollege()));
		// 设置学号
		apply.setStudentNo(applyCtr.getStudentNo());
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
	 * @param apply 从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public ApplicationCtr applyDaoToCtr(Application apply) {
		// 根据itemId获取项目具体信息
		ItemCtr itemCtr = itemService.getItemByItemId(apply.getItemId(), 1);
		// 转换为Dao层的pojo
		ApplicationCtr applyCtr = new ApplicationCtr();
		// 设置项目信息
		applyCtr.setId(apply.getId());
		applyCtr.setGame(itemCtr.getGame());
		applyCtr.setCategory(itemCtr.getCategory());
		applyCtr.setItem(itemCtr.getItem());
		applyCtr.setItemId(apply.getItemId());
		// 设置用户Id
		applyCtr.setStudentNo(apply.getStudentNo());
		// 设置职位
		applyCtr.setJob(jobService.jobIntToString(apply.getJob()));
		// 设置性别
		applyCtr.setGender(genderGroupService.genderIntToStr(apply.getGender()));
		// 设置姓名
		applyCtr.setName(apply.getName());
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
	public SGResult checkData(Application apply) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();

		criteria.andJobEqualTo(apply.getJob());
		criteria.andStudentNoEqualTo(apply.getStudentNo());
		criteria.andItemIdEqualTo(apply.getItemId());
		// 执行查询
		List<Application> list = ApplicationMapper.selectByExample(example);
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
	public SGResult addApply(ApplicationCtr applyCtr) {
		Application apply = applyCtrToDao(applyCtr);

		// 数据有效性检验
		if (StringUtils.isBlank(apply.getStudentNo()) || StringUtils.isBlank(apply.getJob().toString())
				|| StringUtils.isBlank(apply.getItemId().toString()) || StringUtils.isBlank(apply.getName().toString()))
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
		ApplicationMapper.insert(apply);
		// 返回成功
		return SGResult.build(200, "报名成功", apply);
	}

	/**
	 * 根据学号查询已报名比赛项目列表
	 * 
	 * @param 学号
	 * @return 根据学号查找到的学生已报名比赛项目信息
	 */
	public List<ApplicationCtr> getApplicationListByStudentNo(String studentno) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentNoEqualTo(studentno);
		criteria.andStatusNotEqualTo(0);
		List<Application> list = ApplicationMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		List<ApplicationCtr> result = new ArrayList<>();
		for (Application apply : list) {
			ApplicationCtr applyCtr = applyDaoToCtr(apply);
			result.add(applyCtr);
		}
		return result;
	}

	/**
	 * 查询报名某一项目的总人数
	 */
	@Override
	public Long countByitem(Long itemId) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		criteria.andStatusGreaterThanOrEqualTo(1);
		return ApplicationMapper.countByExample(example);
	}

	/**
	 * 查询报名情况 groupByItem
	 */
	@Override
	public Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItem(List<ItemCtr> itemsCtr) {
		Map<Map<Map<String, String>, String>, Long> result = new HashMap<>();

		Map<String, String> str = new HashMap<>();
		str.put("total", "total");

		Map<Map<String, String>, String> str2 = new HashMap<>();
		str2.put(str, "total");

		Map<Map<String, String>, String> str3 = new HashMap<>();
		str3.put(str, "need");

		// 报名的总人数
		Long allTotal = (long) 0;

		// 需报名的总人数
		Long allNeed = (long) 0;

		for (ItemCtr itemCtr : itemsCtr) {
			// 检查项目状态
			if (itemCtr.getDate().before(new Date()) || itemCtr.getStatus() != 1)
				continue;

			// 得到项目信息
			Map<String, String> itemInfo = new HashMap<>();
			itemInfo.put(itemCtr.getItem(), itemCtr.getGender());

			ApplicationExample example = new ApplicationExample();
			Criteria criteria = example.createCriteria();
			criteria.andItemIdEqualTo(itemCtr.getId());
			criteria.andStatusGreaterThanOrEqualTo(1);
			Long itemTotalNum = ApplicationMapper.countByExample(example);

			// 将该项目的总报名人数加入到结果中
			Map<Map<String, String>, String> itemTotal = new HashMap<>();
			itemTotal.put(itemInfo, "total");
			result.put(itemTotal, itemTotalNum);

			// 将该项目的需报名人数加入到结果中
			Map<Map<String, String>, String> itemNeed = new HashMap<>();
			itemNeed.put(itemInfo, "need");
			result.put(itemNeed, (long) itemCtr.getParticipantNum());

			allTotal = allTotal + itemTotalNum;
			allNeed = allNeed + itemCtr.getParticipantNum();
		}
		result.put(str2, allTotal);
		result.put(str3, allNeed);
		return result;
	}

	/**
	 * 查询报名情况（详细） groupByItem
	 */
	@Override
	public Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItemDetail(List<ItemCtr> itemsCtr) {
		Map<Map<Map<String, String>, String>, Long> result = new HashMap<>();

		Map<String, String> str = new HashMap<>();
		str.put("total", "total");

		Map<Map<String, String>, String> str2 = new HashMap<>();
		str2.put(str, "total");

		Map<Map<String, String>, String> str3 = new HashMap<>();
		str3.put(str, "need");

		// 报名的总人数
		Long allTotal = (long) 0;

		// 需报名的总人数
		Long allNeed = (long) 0;

		// 获取所有学院
		Map<Integer, String> allCollegesIdAndName = collegeService.getAllCollegesIdAndName();
		Set<Integer> collegesId = allCollegesIdAndName.keySet();

		for (ItemCtr itemCtr : itemsCtr) {
			// 检查项目状态
			if (itemCtr.getDate().before(new Date()) || itemCtr.getStatus() != 1)
				continue;

			Long itemTotalNum = (long) 0;

			// 得到比赛信息
			Map<String, String> itemInfo = new HashMap<>();
			itemInfo.put(itemCtr.getItem(), itemCtr.getGender());

			for (Integer collegeId : collegesId) {
				// 设置项目信息表
				String collegeName = allCollegesIdAndName.get(collegeId);

				// 查询该学院该项目报名人数
				ApplicationExample example = new ApplicationExample();
				Criteria criteria = example.createCriteria();
				criteria.andCollegeEqualTo(collegeId);
				criteria.andItemIdEqualTo(itemCtr.getId());
				criteria.andStatusGreaterThanOrEqualTo(1);
				Long count = ApplicationMapper.countByExample(example);

				// 如果有人报名，加入到结果中
				if (count != 0) {
					itemTotalNum = itemTotalNum + count;

					Map<Map<String, String>, String> collegeAndItemInfo = new HashMap<>();
					collegeAndItemInfo.put(itemInfo, collegeName);
					result.put(collegeAndItemInfo, count);
				}
			}

			// 将该项目的总报名人数加入到结果中
			Map<Map<String, String>, String> itemTotal = new HashMap<>();
			itemTotal.put(itemInfo, "total");
			result.put(itemTotal, itemTotalNum);

			// 将该项目的需报名人数加入到结果中
			Map<Map<String, String>, String> itemNeed = new HashMap<>();
			itemNeed.put(itemInfo, "need");
			result.put(itemNeed, (long) itemCtr.getParticipantNum());

			allTotal = allTotal + itemTotalNum;
			allNeed = allNeed + itemCtr.getParticipantNum();
		}
		result.put(str2, allTotal);
		result.put(str3, allNeed);
		return result;
	}

	/**
	 * 查询报名情况 groupByCollege
	 */
	@Override
	public Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollege(List<ItemCtr> itemsCtr) {
		Map<Map<String, Map<String, String>>, Long> result = new HashMap<>();

		Map<String, String> str = new HashMap<>();
		str.put("total", "total");

		Map<String, Map<String, String>> str2 = new HashMap<>();
		str2.put("total", str);

		// 报名的总人数
		Long allTotal = (long) 0;

		// 获取所有学院
		Map<Integer, String> allCollegesIdAndName = collegeService.getAllCollegesIdAndName();
		Set<Integer> collegesId = allCollegesIdAndName.keySet();

		for (Integer collegeId : collegesId) {
			// 得到学院名
			String collegeName = allCollegesIdAndName.get(collegeId);

			ApplicationExample example = new ApplicationExample();

			for (ItemCtr itemCtr : itemsCtr) {
				// 检查项目状态
				if (itemCtr.getDate().before(new Date()) || itemCtr.getStatus() != 1)
					continue;

				Criteria criteria = example.or();
				criteria.andCollegeEqualTo(collegeId);
				criteria.andItemIdEqualTo(itemCtr.getId());
				criteria.andCollegeGreaterThanOrEqualTo(1);
			}

			// 查询该学院该项目报名人数
			Long collegeTotalNum = ApplicationMapper.countByExample(example);

			// 将该学院的总报名人数加入到结果中
			if (collegeTotalNum != 0) {
				Map<String, Map<String, String>> collegeTotal = new HashMap<>();
				collegeTotal.put(collegeName, str);
				result.put(collegeTotal, collegeTotalNum);

				allTotal = allTotal + collegeTotalNum;
			}
		}
		result.put(str2, allTotal);
		return result;
	}

	/**
	 * 查询报名情况（详细） groupByCollege
	 */
	@Override
	public Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollegeDetail(
			List<ItemCtr> itemsCtr) {
		Map<Map<String, Map<String, String>>, Long> result = new HashMap<>();

		Map<String, String> str = new HashMap<>();
		str.put("total", "total");

		Map<String, Map<String, String>> str2 = new HashMap<>();
		str2.put("total", str);

		// 报名的总人数
		Long allTotal = (long) 0;

		// 获取所有学院
		Map<Integer, String> allCollegesIdAndName = collegeService.getAllCollegesIdAndName();
		Set<Integer> collegesId = allCollegesIdAndName.keySet();

		for (Integer collegeId : collegesId) {
			Long collegeTotalNum = (long) 0;

			// 得到学院名
			String collegeName = allCollegesIdAndName.get(collegeId);

			for (ItemCtr itemCtr : itemsCtr) {
				// 检查项目状态
				if (itemCtr.getDate().before(new Date()) || itemCtr.getStatus() != 1)
					continue;

				// 设置项目信息表
				Map<String, String> itemInfo = new HashMap<>();
				itemInfo.put(itemCtr.getItem(), itemCtr.getGender());

				// 查询该学院该项目报名人数
				ApplicationExample example = new ApplicationExample();
				Criteria criteria = example.createCriteria();
				criteria.andCollegeEqualTo(collegeId);
				criteria.andItemIdEqualTo(itemCtr.getId());
				criteria.andCollegeGreaterThanOrEqualTo(1);
				Long count = ApplicationMapper.countByExample(example);

				// 如果有人报名，加入到结果中
				if (count != 0) {
					collegeTotalNum = collegeTotalNum + count;

					Map<String, Map<String, String>> collegeAndItemInfo = new HashMap<>();
					collegeAndItemInfo.put(collegeName, itemInfo);
					result.put(collegeAndItemInfo, count);
				}
			}

			// 将该学院的总报名人数加入到结果中
			if (collegeTotalNum != 0) {
				Map<String, Map<String, String>> collegeTotal = new HashMap<>();
				collegeTotal.put(collegeName, str);
				result.put(collegeTotal, collegeTotalNum);

				allTotal = allTotal + collegeTotalNum;
			}
		}
		result.put(str2, allTotal);
		return result;
	}

	/**
	 * 维护报名表（将已结束或已删除项目报名数据的状态设置为“已删除”）
	 */
	@Override
	public SGResult maintenanceApply(List<Long> itemsId) {
		ApplicationExample example = new ApplicationExample();

		for (Long itemId : itemsId) {
			Criteria criteria = example.or();
			criteria.andItemIdEqualTo(itemId);
			criteria.andStatusEqualTo(1);
		}

		List<Application> list = ApplicationMapper.selectByExample(example);

		for (Application application : list) {
			application.setStatus(0);
			// 0-已删除（或已通过校级审核），1-正在审核，2-院级审核已通过，3-校级审核已通过
			application.setUpdated(new Date());
			ApplicationMapper.updateByPrimaryKeySelective(application);
		}
		return SGResult.build(200, "维护报名表成功！", list);
	}

	/**
	 * 硬删除状态为（0）的报名表
	 */
	@Override
	public SGResult hardDeleteApply() {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<Application> list = ApplicationMapper.selectByExample(example);

		for (Application application : list) {
			ApplicationMapper.deleteByPrimaryKey(application.getId());
		}

		return SGResult.build(200, "硬删除报名表成功！");
	}

	/***
	 * 院级管理员审核
	 */
	@Override
	public SGResult reviewByCollegeManager(Long ids[]) {
		if (ids == null || ids.length == 0)
			return SGResult.build(200, "请先选择要审核的报名记录！");

		Application applicationTemplate = new Application();
		applicationTemplate.setStatus(2);
		applicationTemplate.setUpdated(new Date());

		ApplicationExample example = new ApplicationExample();

		for (Long id : ids) {
			Criteria criteria = example.or();
			criteria.andStatusEqualTo(1);
			criteria.andIdEqualTo(id);
		}

		ApplicationMapper.updateByExampleSelective(applicationTemplate, example);

		return SGResult.build(200, "院级管理员审核完成！");
	}

	/***
	 * 校级管理员审核
	 */
	@Override
	public List<Application> reviewByUniversityManager(Long ids[]) {
		if (ids == null || ids.length == 0)
			return null;

		Application applicationTemplate = new Application();
		applicationTemplate.setStatus(3);
		applicationTemplate.setUpdated(new Date());

		ApplicationExample example = new ApplicationExample();
		for (Long id : ids) {
			Criteria criteria = example.or();
			criteria.andStatusEqualTo(2);
			criteria.andIdEqualTo(id);
		}
		ApplicationMapper.updateByExampleSelective(applicationTemplate, example);

		ApplicationExample exampleOut = new ApplicationExample();
		for (Long id : ids) {
			Criteria criteria = exampleOut.or();
			criteria.andIdEqualTo(id);
		}
		return ApplicationMapper.selectByExample(exampleOut);
	}

	/**
	 * 根据项目id、状态和学院获取报名记录
	 */
	@Override
	public List<Application> getApplicationListByItemsId(List<Long> itemsId, Integer status, String college) {
		ApplicationExample example = new ApplicationExample();
		for (Long itemId : itemsId) {
			Criteria criteria = example.or();
			criteria.andStatusNotEqualTo(0);
			criteria.andItemIdEqualTo(itemId);
			if (status != null)
				criteria.andStatusEqualTo(status);
			if(!StringUtils.isBlank(college))
				criteria.andCollegeEqualTo(collegeService.getId(college));
		}
		return ApplicationMapper.selectByExample(example);
	}

}
