package cn.smartGym.service;

import java.util.List;

import cn.smartGym.controller.pojoCtr.SgUserCtr;
import cn.smartGym.pojo.SgUser;
import common.utils.SGResult;

/**
 * 用户管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface UserService {

	SgUser userCtrToDao(SgUserCtr userCtr);

	SgUserCtr userDaoToCtr(SgUser userDao);

	SGResult decodeUserInfo(SgUserCtr userCtr);

	SGResult checkData(String param, int type);

	SGResult register(SgUser user);
	
	SGResult deleteUserByWxId(String wxId);
	
	SGResult deleteUserByStudentNo(String studentNo);
	
	SGResult hardDeleteUser();
	
	SGResult update(SgUser user);
	
	SGResult selectByWxId(String id);
	
	SGResult selectByStudentNo(String studentNo);
	
//	Integer getCollegeByStudentNo(String studentNo);
	
	SGResult getUser(SgUser managerUser, String studentNoSelected);
	
	SGResult setUserAuthority(String studentNo, Integer authority);
	
	List<SgUserCtr> getUserList(SgUser user);
}
