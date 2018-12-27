package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import common.utils.SGResult;

/**
 * 用户管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface UserService {
	SGResult checkData(String param, int type);

	SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr);

	SmartgymUsersCtr userDaoToCtr(SmartgymUsers userDao);

	SGResult decodeUserInfo(SmartgymUsersCtr userCtr);

	SGResult register(SmartgymUsers user);
}
