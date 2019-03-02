package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.SgUser;
import common.utils.SGResult;

/**
 * 用户管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface UserService {

	SGResult register(SgUser user);

	SGResult deleteUserByDtail(SgUser user);

	void hardDeleteUser();

	SGResult setUserAuthority(Integer authority, String... studentNos);

	SGResult update(SgUser user);

	SGResult checkData(String param, int type);

	SGResult getUserByDtail(SgUser user);

	SGResult getUserByManagerAndStudentNos(SgUser managerUser, String... studentNos);
	
	List<SgUser> getUsersByDetailsAndStatuses(SgUser user, Integer... statuses);

	List<String> getStudentNosByUsers(List<SgUser> users);

}
