package cn.smartGym.service;

import java.util.List;
import java.util.Map;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojoCtr.ApplicationCtr;
import cn.smartGym.pojoCtr.ItemCtr;
import common.utils.SGResult;

/**
 * 比赛报名服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface ApplyService {
	Application applyCtrToDao(ApplicationCtr apply);

	ApplicationCtr applyDaoToCtr(Application apply);

	SGResult checkData(Application apply);

	SGResult addApply(ApplicationCtr applyCtr);

	SGResult hardDeleteApply();

	SGResult maintenanceApply(List<Long> itemsId);

	List<ApplicationCtr> getApplicationListByStudentNo(String studentno);

	List<Application> getApplicationListByItemsId(List<Long> itemsId, Integer status, String college);

	Long countByitem(Long itemId);

	Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItem(List<ItemCtr> itemsCtr);

	Map<Map<Map<String, String>, String>, Long> getApplyNumGroupByItemDetail(List<ItemCtr> itemsCtr);

	Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollege(List<ItemCtr> itemsCtr);

	Map<Map<String, Map<String, String>>, Long> getApplyNumGroupByCollegeDetail(List<ItemCtr> itemsCtr);

	SGResult reviewByCollegeManager(Long ids[]);

	List<Application> reviewByUniversityManager(Long ids[]);
}
