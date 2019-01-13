package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoctr.request.UserCtr;
import common.utils.SGResult;

/**
 * 用户管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface UserService {

	SgUser userCtrToDao(UserCtr userCtr);

	UserCtr userDaoToCtr(SgUser userDao);

	SGResult decodeUserInfo(UserCtr userCtr);

	SGResult checkData(String param, int type);

	SGResult register(UserCtr userCtr);
	
	SGResult deleteUser(String wxId);
	
	SGResult hardDeleteUser();
	
	List<SgUser> selectByWxid(String id);
	
	SGResult update(UserCtr userCtr);
	
	Integer getCollegeByStudentNo(String studentNo);
	
	SGResult getUser(UserCtr userCtr, String studentNoSelected);
	
	SGResult setUserAuthority(String studentNo, Integer authority);
	
	List<UserCtr> getUserList(UserCtr userCtr);
}
