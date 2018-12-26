package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import common.utils.SGResult;

/**
 * 比赛报名服务层
 *
 */
public interface ApplyService {
	SGResult addApply(SmartgymApplications apply);
	SGResult checkData(SmartgymApplications apply);
	SmartgymApplications applyCtrtoDao(SmartgymApplicationsCtr apply);
	SmartgymApplicationsCtr applyDaotoCtr(SmartgymApplications apply);
}