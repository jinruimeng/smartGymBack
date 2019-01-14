package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojoctr.request.ApplicationCtr;
import cn.smartGym.pojoctr.request.ItemCtr;
import cn.smartGym.pojoctr.response.ApplicationInfo;
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

	List<ApplicationCtr> getApplicationListByItemsId(List<Long> itemsId, String college, Integer... statuses);

	Long countByitem(Long itemId);

	List<ApplicationInfo> getApplyNumGroupByItem(List<ItemCtr> itemsCtr);

	List<ApplicationInfo> getApplyNumGroupByItemDetail(ItemCtr itemCtr);

	List<ApplicationInfo> getApplyNumGroupByCollege(List<ItemCtr> itemsCtr);

	List<ApplicationInfo> getApplyNumGroupByCollegeDetail(List<ItemCtr> itemsCtr, String college);

	SGResult reviewByCollegeManager(Long ids[]);

	List<Application> reviewByUniversityManager(List<Long> itemsId);
}
