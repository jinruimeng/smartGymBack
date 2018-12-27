package cn.smartGym.service.impl;

import org.springframework.stereotype.Service;

import cn.smartGym.service.JobService;

@Service
public class JobServiceImpl implements JobService {

	@Override
	public Integer jobStringToInt(String job) {
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

	@Override
	public String jobIntToString(Integer job) {
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
