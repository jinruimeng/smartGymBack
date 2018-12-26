package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import common.utils.SGResult;

public interface UserService {
	SGResult checkData(String param, int type);
	SGResult register(SmartgymUsers user);
	SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr);
	SmartgymUsersCtr userDaoToCtr(SmartgymUsers userDao);
}
