package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymUsersMapper;
import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojo.SmartgymUsersExample;
import cn.smartGym.pojo.SmartgymUsersExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ManagerService;
import cn.smartGym.service.UserService;
import common.utils.SGResult;

/**
 * 管理员管理服务层
 * @author ikangkang
 *
 */
@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private SmartgymUsersMapper userMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private CollegeService collegeService;
	
	/**
	 * 管理员根据学号查找用户信息
	 * @param userCtr 管理员信息
	 * @param studentNo 要查询的用户学号
	 */
	public SGResult getUser(SmartgymUsersCtr userCtr, String studentNo) {
		//得到管理员的权限级别0-普通用户  1-院级管理员  2-校级管理员  3-开发者
		Integer authority = userCtr.getAuthority();
		//得到管理员的学院
		Integer college = collegeService.getId(userCtr.getCollege());
		//根据学号查询结果
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andStudentNoEqualTo(studentNo);
		List<SmartgymUsers> list = userMapper.selectByExample(example);
		if(list == null || list.size() <= 0)
			return SGResult.build(404, "没有查到该用户，请重新输入学号！");
		//查到用户
		SmartgymUsers user = list.get(0);
		//如果用户的权限高于管理员，或者用户所属学院与管理员不在同一学院，则不予返回
		if(user.getAuthority() >= authority || user.getCollege() != college)
			return SGResult.build(404, "没有查到该用户，您权限不够！");
		return SGResult.build(200, "查询成功！", userService.userDaoToCtr(user));
	}
	
	/**
	 * 设置用户权限
	 * @param authority 0-普通用户  1-院级管理员  2-校级管理员  3-开发者
	 */
	public SGResult setUserAuthority(SmartgymUsersCtr userCtr) {
		userMapper.updateByPrimaryKeySelective(userService.userCtrToDao(userCtr));
		return SGResult.build(200, "设置权限成功！");
	}
	
	/**
	 * 根据前端提供的用户信息返回User列表
	 * @param authority 0-普通用户  1-院级管理员  2-校级管理员  3-开发者
	 */
	public List<SmartgymUsersCtr> getUserList(SmartgymUsersCtr userCtr) {
		//取出权限
		Integer authority = userCtr.getAuthority();
		//查询结果集
		List<SmartgymUsers> list = new ArrayList<>();
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		//如果是开发者,则返回所有的User
		if(authority == 3) {
			criteria.andStatusEqualTo(1);
			list = userMapper.selectByExample(example);
		} else if(authority == 2) {
			//如果是校级管理员，则返回校级管理员以下的所有User
			criteria.andStatusEqualTo(1);
			criteria.andAuthorityLessThan(2);
			list = userMapper.selectByExample(example);
		}else if(authority == 1) {
			//如果开发者是院级管理员，则查出该院所有院级管理员以下的User
			//得到该管理员所属的院系
			String collegeName = userCtr.getCollege();
			Integer college = collegeService.getId(collegeName);
			//根据院系和权限去查询
			criteria.andStatusEqualTo(1);
			criteria.andAuthorityLessThan(1);
			criteria.andCollegeEqualTo(college);
			list = userMapper.selectByExample(example);
		} else {
			list.add(userService.userCtrToDao(userCtr));
		}
		//返回给表现层
		List<SmartgymUsersCtr> listCtr = new ArrayList<>();
		for (SmartgymUsers user : list) {
			listCtr.add(userService.userDaoToCtr(user));
		}	
		return listCtr;
	}

	/**
	 * 设置用户权限
	 */
//	public SGResult setUsersAuthority(List<SmartgymUsersCtr> usersCtr) {
//		for (SmartgymUsersCtr userCtr : usersCtr) {
//			SmartgymUsers user = userService.userCtrToDao(userCtr);
//			userMapper.updateByPrimaryKeySelective(user);
//		}
//		return SGResult.build(200, "设置权限成功！");
//	}
}
