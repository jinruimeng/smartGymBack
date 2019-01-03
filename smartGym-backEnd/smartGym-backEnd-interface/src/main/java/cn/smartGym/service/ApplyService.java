package cn.smartGym.service;

import java.util.List;
import java.util.Map;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import common.utils.SGResult;

/**
 * 比赛报名服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface ApplyService {
	SmartgymApplications applyCtrToDao(SmartgymApplicationsCtr apply);

	SmartgymApplicationsCtr applyDaoToCtr(SmartgymApplications apply);

	SGResult checkData(SmartgymApplications apply);

	SGResult addApply(SmartgymApplicationsCtr applyCtr);

	SGResult hardDeleteApply();

	SGResult maintenanceApply(List<Long> itemsId);

	List<SmartgymApplicationsCtr> getApplycationListByStudentNo(String studentno);

	List<SmartgymApplications> getApplycationListByItemsId(List<Long> itemsId, Integer status, String college);

	Long countByitem(Long itemId);

	Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItem(List<SmartgymItemsCtr> itemsCtr);

	Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItemDetail(List<SmartgymItemsCtr> itemsCtr);

	Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollege(List<SmartgymItemsCtr> itemsCtr);

	Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollegeDetail(List<SmartgymItemsCtr> itemsCtr);

	SGResult reviewByCollegeManager(Long ids[]);

	List<SmartgymApplications> reviewByUniversityManager(Long ids[]);
}
