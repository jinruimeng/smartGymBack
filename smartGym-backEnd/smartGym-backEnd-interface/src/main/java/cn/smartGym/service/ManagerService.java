package cn.smartGym.service;
/**
 * 管理员管理服务层
 * @author ikangkang
 *
 */

import java.util.List;

import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import common.utils.SGResult;

public interface ManagerService {
	
	SGResult getUser(SmartgymUsersCtr userCtr, String studentNoSelected);
	
	public SGResult setUserAuthority(String studentNo, Integer authority);
	
	List<SmartgymUsersCtr> getUserList(SmartgymUsersCtr userCtr);
	
//	SGResult setUsersAuthority(List<SmartgymUsersCtr> usersCtr);
	
}
