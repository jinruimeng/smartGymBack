package cn.smartGym.service;

import java.util.List;

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

	SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr);

	SmartgymUsersCtr userDaoToCtr(SmartgymUsers userDao);

	SGResult decodeUserInfo(SmartgymUsersCtr userCtr);

	SGResult checkData(String param, int type);

	SGResult register(SmartgymUsers user);
	
	List<SmartgymUsers> selectByWxid(String id);
}
