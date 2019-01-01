package cn.smartGym.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.smartGym.service.JobService;

/**
 * 职位管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class JobServiceImpl implements JobService {

	/**
	 * 职位 字符串到编号转换
	 */
	@Override
	public Integer jobStringToInt(String job) {
		if (StringUtils.isBlank(job))
			return null;

		switch (job) {
		case "队员":
			return 0;
		case "领队":
			return 1;
		case "教练":
			return 2;
		case "联系人员":
			return 3;
		case "工作人员":
			return 4;
		default:
			return 0;
		}
	}

	/**
	 * 职位 编号到字符串转换
	 */
	@Override
	public String jobIntToString(Integer job) {
		if (job == null)
			return null;

		switch (job) {
		case 0:
			return "队员";
		case 1:
			return "领队";
		case 2:
			return "教练";
		case 3:
			return "联系人员";
		case 4:
			return "工作人员";
		default:
			return "队员";
		}
	}
}
