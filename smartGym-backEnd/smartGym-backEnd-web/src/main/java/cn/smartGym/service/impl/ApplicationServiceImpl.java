package cn.smartGym.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ItemService;
import common.enums.Gender;
import common.enums.Job;
import common.utils.ExcelHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.ApplicationMapper;
import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.ApplicationExample;
import cn.smartGym.pojo.ApplicationExample.Criteria;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoCtr.response.ApplicationInfo;
import cn.smartGym.service.ApplicationService;
import cn.smartGym.utils.CollegeAndCampusUtils;
import common.enums.ErrorCode;
import common.enums.GenderGroup;
import common.utils.IDUtils;
import common.utils.SGResult;

/**
 * 比赛报名管理Service
 *
 * @author ikangkang
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ApplicationMapper applicationMapper;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CollegeService collegeService;

	/**
	 * 报名比赛
	 *
	 * @param 申请信息
	 * @return 返回给前端的信息 {status, msg, data}
	 */
	@Override
	public SGResult addApplication(Application application) {
		// 数据有效性检验
		if (StringUtils.isBlank(application.getStudentNo()) || StringUtils.isBlank(application.getJob().toString())
				|| StringUtils.isBlank(application.getItemId().toString())
				|| StringUtils.isBlank(application.getName()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "报名信息不完整，报名失败！");
		SGResult result = checkData(application);
		if (!result.isOK()) {
			return result;
		}

		// 生成比赛报名id
		final long applicationId = IDUtils.genId();
		// 补全application其他属性
		application.setId(applicationId);
		application.setStatus(1);
		// 0-已删除（或已通过校级审核）1-正在审核 2-院级审核通过 3-校级审核通过
		application.setCreated(new Date());
		application.setUpdated(new Date());
		// 插入数据库
		applicationMapper.insert(application);
		// 返回成功
		return SGResult.ok("报名成功！");
	}

	/**
	 * 检查是否已报名该项目
	 *
	 * @param 申请信息
	 * @return 返回信息
	 */
	@Override
	public SGResult checkData(Application application) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();

		criteria.andJobEqualTo(application.getJob());
		criteria.andStudentNoEqualTo(application.getStudentNo());
		criteria.andItemIdEqualTo(application.getItemId());
		criteria.andStatusNotEqualTo(0);
		// 执行查询
		List<Application> list = applicationMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (list != null && list.size() > 0) {
			// 如果有数据返回false
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "已报名该项目！");
		}
		// 如果没有数据返回true
		return SGResult.ok();
	}

	/**
	 * 硬删除状态为（0）的报名表
	 */
	@Override
	public void hardDeleteApplication() {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		applicationMapper.deleteByExample(example);
	}

	/**
	 * 删除已报名项目
	 *
	 * @param 申请信息
	 * @return 返回信息
	 */
	@Override
	public SGResult deleteApplication(Application application) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();

		if (application.getId() != null)
			criteria.andIdEqualTo(application.getId());
		if (application.getStudentNo() != null)
			criteria.andStudentNoEqualTo(application.getStudentNo());
		if (application.getItemId() != null)
			criteria.andItemIdEqualTo(application.getItemId());
		if (application.getJob() != null)
			criteria.andJobEqualTo(application.getJob());
		if (application.getStatus() != null)
			criteria.andStatusEqualTo(application.getStatus());
		else
			criteria.andStatusNotEqualTo(0);

		Application applicationExample = new Application();
		applicationExample.setStatus(0);
		applicationExample.setUpdated(new Date());
		applicationMapper.updateByExampleSelective(applicationExample, example);

		return SGResult.ok();
	}

	public SGResult updateUser(SgUser userUpdate) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();

		if (StringUtils.isNotBlank(userUpdate.getStudentNo()))
			criteria.andStudentNoEqualTo(userUpdate.getStudentNo());

		Application applicationExample = new Application();
		applicationExample.setName(userUpdate.getName());
		applicationExample.setCollege(userUpdate.getCollege());
		applicationExample.setUpdated(new Date());
		applicationMapper.updateByExampleSelective(applicationExample, example);

		return SGResult.ok();
	}

	/**
	 * 维护报名表（软删除已结束或已删除项目、已删除用户的报名记录）
	 */
	@Override
	public void maintenanceApplication(List<Long> itemIds, List<String> studentNos) {
		ApplicationExample example = new ApplicationExample();

		Criteria criteriaItemIds = example.or();
		if (itemIds != null && itemIds.size() != 0)
			criteriaItemIds.andItemIdNotIn(itemIds);
		criteriaItemIds.andStatusNotEqualTo(0);

		Criteria criteriaStudentNos = example.or();
		if (studentNos != null && studentNos.size() != 0)
			criteriaStudentNos.andStudentNoNotIn(studentNos);
		criteriaStudentNos.andStatusNotEqualTo(0);

		List<Application> list = applicationMapper.selectByExample(example);

		for (Application application : list) {
			application.setStatus(0);
			// 0-已删除，1-正在审核，2-院级审核已通过，3-校级审核已通过
			application.setUpdated(new Date());
			applicationMapper.updateByPrimaryKeySelective(application);
		}
	}

	/**
	 * 根据学号查询已报名比赛项目列表
	 *
	 * @param 学号
	 * @return 根据学号查找到的学生已报名比赛项目信息
	 */
	public List<Application> getApplicationListByStatusAndStudentNo(Integer status, String... studentNos) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);
		if (studentNos != null && studentNos.length != 0)
			criteria.andStudentNoIn(Arrays.asList(studentNos));
		if (status != null)
			criteria.andStatusEqualTo(status);
		example.setOrderByClause("updated DESC");
		List<Application> ApplicationList = applicationMapper.selectByExample(example);
		return ApplicationList;
	}

	/**
	 * 根据项目id、学院和状态获取报名记录
	 */
	@Override
	public List<Application> getApplicationListByItemIdsAndCollegeAndStatus(List<Long> itemIds, String college,
			Integer... statuses) {
		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		if (itemIds != null && itemIds.size() != 0)
			criteria.andItemIdIn(itemIds);
		if (statuses != null & statuses.length != 0)
			criteria.andStatusIn(Arrays.asList(statuses));
		if (!StringUtils.isBlank(college))
			if (college != null && !college.equals("total"))
				criteria.andCollegeEqualTo(CollegeAndCampusUtils.getCollegeIndex(college));

		example.setOrderByClause("updated DESC");
		List<Application> applications = applicationMapper.selectByExample(example);

		return applications;
	}

	/***
	 * 院级管理员审核（将报名表状态置为2）
	 */
	@Override
	public SGResult reviewByCollegeManager(List<Long> ids) {
		if (ids == null || ids.size() == 0)
			return SGResult.ok("请先选择要审核的报名记录！");

		Application applicationTemplate = new Application();
		applicationTemplate.setStatus(2);
		applicationTemplate.setUpdated(new Date());

		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andIdIn(ids);

		applicationMapper.updateByExampleSelective(applicationTemplate, example);

		return SGResult.ok("院级管理员审核完成！");
	}

	/***
	 * 校级管理员审核
	 */
	@Override
	public List<Application> reviewByUniversityManager(List<Long> itemIds) {
		List<Application> applications = getApplicationListByItemIdsAndCollegeAndStatus(itemIds, null, 2);
		List<Long> applicationIds = getIdsByApplications(applications);

		if (applicationIds == null || applicationIds.isEmpty())
			return null;

		Application applicationTemplate = new Application();
		applicationTemplate.setStatus(3);
		applicationTemplate.setUpdated(new Date());

		ApplicationExample example = new ApplicationExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(2);
		criteria.andIdIn(applicationIds);

		List<Application> applicationsOfStatus2 = applicationMapper.selectByExample(example);
		applicationMapper.updateByExampleSelective(applicationTemplate, example);

		return applicationsOfStatus2;
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
		criteria.andJobEqualTo(0);
		return applicationMapper.countByExample(example);
	}

	/**
	 * 查询报名情况 groupByItem
	 */
	@Override
	public List<ApplicationInfo> getApplicationNumGroupByItem(List<Item> items) {
		List<ApplicationInfo> result = new ArrayList<>();

		// 报名的总人数
		Long totalApplication = (long) 0;

		// 已审核的总人数
		Long totalReview = (long) 0;

		// 需报名的总人数
		Integer totalNeed = 0;

		for (Item item : items) {
			// 检查项目状态、排除非运动员
			if (item.getDate().before(new Date()) || item.getStatus() != 1 || "非运动员".equals(item.getCategory()))
				continue;

			// 得到项目信息
			ApplicationInfo applicationInfo = new ApplicationInfo();
			applicationInfo.setItemId(item.getId());
			applicationInfo.setItem(item.getItem());
			applicationInfo.setGender(GenderGroup.getName(item.getGender()));

			// 获取需报名人数
			applicationInfo.setNeedNumber(item.getParticipantNum());
			totalNeed += item.getParticipantNum();

			// 获取报名人数
			ApplicationExample exampleApplication = new ApplicationExample();
			Criteria criteriaApplication = exampleApplication.createCriteria();
			criteriaApplication.andItemIdEqualTo(item.getId());
			criteriaApplication.andStatusGreaterThanOrEqualTo(1);
			// 排除非运动员
			criteriaApplication.andJobEqualTo(0);
			Long applicationNum = applicationMapper.countByExample(exampleApplication);
			totalApplication += applicationNum;

			// 获取已审核人数
			ApplicationExample exampleReview = new ApplicationExample();
			Criteria criteriaReview = exampleReview.createCriteria();
			criteriaReview.andItemIdEqualTo(item.getId());
			criteriaReview.andStatusGreaterThanOrEqualTo(2);
			// 排除非运动员
			criteriaReview.andJobEqualTo(0);
			Long reviewNum = applicationMapper.countByExample(exampleReview);
			totalReview += reviewNum;

			// 将该项目的总报名人数加入到结果中
			applicationInfo.setApplicationNumber(applicationNum);
			applicationInfo.setReviewNumber(reviewNum);
			result.add(applicationInfo);
		}

		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setItem("Total");
		applicationInfo.setGender("Total");
		applicationInfo.setApplicationNumber(totalApplication);
		applicationInfo.setNeedNumber(totalNeed);
		applicationInfo.setReviewNumber(totalReview);
		result.add(applicationInfo);
		return result;
	}

	/**
	 * 查询报名情况（详细） groupByItem
	 */
	@Override
	public List<ApplicationInfo> getApplicationNumGroupByItemDetail(Item item) {
		List<ApplicationInfo> result = new ArrayList<>();

		// 报名的总人数
		Long totalApplication = (long) 0;

		// 已审核的总人数
		Long totalReview = (long) 0;

		// 获取所有学院
		Map<Integer, String> allCollegesIdAndName = CollegeAndCampusUtils.getAllCollegeIdsAndName();
		Set<Integer> collegesId = allCollegesIdAndName.keySet();

		// 检查项目状态
		if (item.getDate().before(new Date()) || item.getStatus() != 1 || "非运动员".equals(item.getCategory()))
			return result;

		for (Integer collegeId : collegesId) {
			// 设置项目信息表
			String collegeName = allCollegesIdAndName.get(collegeId);

			// 查询该学院该项目报名人数
			ApplicationExample exampleApplication = new ApplicationExample();
			Criteria criteriaApplication = exampleApplication.createCriteria();
			criteriaApplication.andCollegeEqualTo(collegeId);
			criteriaApplication.andItemIdEqualTo(item.getId());
			// 排除非运动员
			criteriaApplication.andJobEqualTo(0);
			criteriaApplication.andStatusGreaterThanOrEqualTo(1);
			Long collegeApplication = applicationMapper.countByExample(exampleApplication);

			// 查询该学院该项目已审核人数
			ApplicationExample exampleReview = new ApplicationExample();
			Criteria criteriaReview = exampleReview.createCriteria();
			criteriaReview.andCollegeEqualTo(collegeId);
			criteriaReview.andItemIdEqualTo(item.getId());
			// 排除非运动员
			criteriaReview.andJobEqualTo(0);
			criteriaReview.andStatusGreaterThanOrEqualTo(2);
			Long collegeReview = applicationMapper.countByExample(exampleReview);

			// 如果有人报名，加入到结果中
			if (collegeApplication != 0) {
				ApplicationInfo applicationInfo = new ApplicationInfo();
				applicationInfo.setCollege(collegeName);
				applicationInfo.setApplicationNumber(collegeApplication);
				applicationInfo.setReviewNumber(collegeReview);
				result.add(applicationInfo);

				totalApplication += collegeApplication;
				totalReview += collegeReview;
			}
		}

		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setCollege("Total");
		applicationInfo.setApplicationNumber(totalApplication);
		applicationInfo.setReviewNumber(totalReview);
		result.add(applicationInfo);

		return result;
	}

	/**
	 * 查询报名情况 groupByCollege
	 */
	@Override
	public List<ApplicationInfo> getApplicationNumGroupByCollege(List<Item> items) {
		List<ApplicationInfo> result = new ArrayList<>();

		// 报名的总人数
		Long allTotalApplication = (long) 0;
		Long allTotalReview = (long) 0;

		// 获取所有学院
		Map<Integer, String> allCollegesIdAndName = CollegeAndCampusUtils.getAllCollegeIdsAndName();
		Set<Integer> collegesId = allCollegesIdAndName.keySet();

		for (Integer collegeId : collegesId) {
			// 得到学院名
			String collegeName = allCollegesIdAndName.get(collegeId);

			ApplicationExample exampleApplication = new ApplicationExample();
			ApplicationExample exampleReview = new ApplicationExample();

			for (Item item : items) {
				// 检查项目状态
				if (item.getDate().before(new Date()) || item.getStatus() != 1)
					continue;

				Criteria criteriaApplication = exampleApplication.or();
				criteriaApplication.andCollegeEqualTo(collegeId);
				criteriaApplication.andItemIdEqualTo(item.getId());
				criteriaApplication.andStatusGreaterThanOrEqualTo(1);
				// 排除非运动员
				criteriaApplication.andJobEqualTo(0);

				Criteria criteriaReview = exampleReview.or();
				criteriaReview.andCollegeEqualTo(collegeId);
				criteriaReview.andItemIdEqualTo(item.getId());
				criteriaReview.andStatusGreaterThanOrEqualTo(2);
				// 排除非运动员
				criteriaReview.andJobEqualTo(0);
			}

			// 查询该学院已报名人数
			Long collegeApplicationNum = applicationMapper.countByExample(exampleApplication);

			// 查询该学院已审核人数
			Long collegeReviewNum = applicationMapper.countByExample(exampleReview);

			// 将该学院的总报名人数加入到结果中
			if (collegeApplicationNum != 0) {
				ApplicationInfo applicationInfo = new ApplicationInfo();
				applicationInfo.setCollege(collegeName);
				applicationInfo.setApplicationNumber(collegeApplicationNum);
				applicationInfo.setReviewNumber(collegeReviewNum);
				result.add(applicationInfo);
				allTotalApplication += collegeApplicationNum;
				allTotalReview += collegeReviewNum;
			}
		}

		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setApplicationNumber(allTotalApplication);
		applicationInfo.setReviewNumber(allTotalReview);
		result.add(applicationInfo);

		return result;
	}

	/**
	 * 查询报名情况（详细） groupByCollege
	 */
	@Override
	public List<ApplicationInfo> getApplicationNumGroupByCollegeDetail(List<Item> items, String college) {
		List<ApplicationInfo> result = new ArrayList<>();

		// 获取学院Id
		Integer collegeId = CollegeAndCampusUtils.getCollegeIndex(college);

		if (collegeId != null) {
			Long collegeTotalApplication = (long) 0;
			Long collegeTotalReview = (long) 0;

			for (Item item : items) {
				// 检查项目状态
				if (item.getDate().before(new Date()) || item.getStatus() != 1)
					continue;

				// 设置项目信息
				ApplicationInfo applicationInfo = new ApplicationInfo();
				applicationInfo.setItem(item.getItem());
				applicationInfo.setGender(GenderGroup.getName(item.getGender()));

				// 查询该学院该项目报名人数
				ApplicationExample exampleApplication = new ApplicationExample();
				Criteria criteriaApplication = exampleApplication.createCriteria();
				criteriaApplication.andStatusGreaterThanOrEqualTo(1);
				criteriaApplication.andJobEqualTo(0);
				criteriaApplication.andCollegeEqualTo(collegeId);
				criteriaApplication.andItemIdEqualTo(item.getId());
				// 排除非运动员
				criteriaApplication.andJobEqualTo(0);
				Long applicationNum = applicationMapper.countByExample(exampleApplication);
				applicationInfo.setApplicationNumber(applicationNum);

				// 查询该学院该项目已审核人数
				ApplicationExample exampleReview = new ApplicationExample();
				Criteria criteriaReview = exampleReview.createCriteria();
				criteriaReview.andStatusGreaterThanOrEqualTo(2);
				criteriaReview.andCollegeEqualTo(collegeId);
				criteriaReview.andItemIdEqualTo(item.getId());
				// 排除非运动员
				criteriaReview.andJobEqualTo(0);
				Long reviewNum = applicationMapper.countByExample(exampleReview);
				applicationInfo.setReviewNumber(reviewNum);

				// 计算学院报名和已审核总人数
				if (applicationNum != 0) {
					collegeTotalApplication += applicationNum;
					collegeTotalReview += reviewNum;
				}
				result.add(applicationInfo);
			}

			// 将总人数加入到结果中
			ApplicationInfo applicationInfo = new ApplicationInfo();
			applicationInfo.setGame("Total");
			applicationInfo.setCategory("Total");
			applicationInfo.setItem("Total");
			applicationInfo.setGender("Total");
			applicationInfo.setApplicationNumber(collegeTotalApplication);
			applicationInfo.setReviewNumber(collegeTotalReview);
			result.add(applicationInfo);
		}
		return result;
	}

	@Override
	public List<Long> getIdsByApplications(List<Application> applications) {
		List<Long> ids = new ArrayList<>();
		for (Application application : applications) {
			ids.add(application.getId());
		}
		return ids;
	}

	@Override
	public void generateApplicationsExcelByCollege(String game, String college) throws IOException {
		// 构建Excel文件路径
		String filePath = getApplicationsExcelFilePath(game, college);
		// 获取Excel标题行数据
		List<String> title = Arrays.asList(ExcelHelper.applicationsExcelTitleArray);
		// 获取applicationsData
		Item item = new Item();
		item.setGame(game);
		List<Item> items = itemService.getItemsByDetailsAndStatuses(item, 1, 2, 3);
		List<Long> itemIds = itemService.getItemIdsByItems(items);
		List<Application> applications = getApplicationListByItemIdsAndCollegeAndStatus(itemIds, college, 2, 3);
		List<List<String>> applicationData = new ArrayList<>();
		for (Application application : applications) {
			applicationData.add(convertApplicationToRowData(application));
		}
		ExcelHelper.writeExcel(title, applicationData, filePath);
	}

	@Override
	public String getApplicationsExcelFilePath(String game, String college) {
		String fileName = String.valueOf((game + college).hashCode());
		String filePath = "downloadFiles/" + fileName + ".xls";
		return filePath;

	}

	/**
	 * 将application对象转化为秩序册EXCEL行数据
	 * 
	 * @param application
	 * @return
	 */
	private List<String> convertApplicationToRowData(Application application) {
		List<String> data = new ArrayList<>();
		data.add(application.getStudentNo());
		data.add(application.getName());
		data.add(Gender.getName(application.getGender()));
		data.add(collegeService.getCollege(application.getCollege()));
		Item item = itemService.getItemByItemIdAndStatuses(application.getItemId());
		data.add(item.getGame());
		data.add(item.getCategory());
		data.add(item.getItem());
		data.add(GenderGroup.getName(item.getGender()));
		data.add(Job.getName(application.getJob()));
		data.add("已审核");
		return data;
	}
}
