package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymUsersMapper;
import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojo.SmartgymUsersExample;
import cn.smartGym.pojo.SmartgymUsersExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderService;
import cn.smartGym.service.UserService;
import common.utils.IDUtils;
import common.utils.SGResult;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SmartgymUsersMapper smartgymUsersMapper;
	@Autowired
	private CampusService campusService;
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private GenderService genderService;
	
	@Override
	public SGResult register(SmartgymUsers user) {
		// 数据有效性检验
		if (StringUtils.isBlank(user.getStudentno()) || StringUtils.isBlank(user.getUsername())
				|| StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getWxid()))
			return SGResult.build(401, "用户数据不完整，注册失败");
		// 1：学号 2：用户名 3：手机号
		SGResult result = checkData(user.getStudentno(), 1);
		if (!(boolean) result.getData()) {
			return SGResult.build(400, "此学号已经被注册");
		}
		result = checkData(user.getPhone(), 3);
		if (!(boolean) result.getData()) {
			return SGResult.build(400, "此手机号已经被占用");
		}
//		result = checkData(user.getUsername(), 2);
//		if (!(boolean) result.getData()) {
//			return SGResult.build(400, "此用户名已经被占用");
//		}

		// 补全pojo属性
		user.setAuthority(0); // 0是普通用户
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setStatus(1); // 0是删除，1是正常
		user.setId(IDUtils.genId());
		// 把用户数据插入数据库
		smartgymUsersMapper.insert(user);
		// 返回添加成功
		return SGResult.build(200, "注册成功");
	}

	@Override
	public SGResult checkData(String param, int type) {
		// 根据不同的type生成不同的查询条件
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		// 1：学号 2：用户名 3：手机号
		if (type == 1) {
			criteria.andStudentnoEqualTo(param);
		} else if (type == 2) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 3) {
			criteria.andPhoneEqualTo(param);
		} else {
			return SGResult.build(400, "数据类型错误");
		}
		// 执行查询
		List<SmartgymUsers> list = smartgymUsersMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (list != null && list.size() > 0) {
			// 如果有数据返回false
			return SGResult.ok(false);
		}
		// 如果没有数据返回true
		return SGResult.ok(true);
	}
	
	@Override
	public SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr) {
		SmartgymUsers user = new SmartgymUsers();

		user.setPhone(userCtr.getPhone());
		user.setStudentno(userCtr.getStudentno());
		user.setWxid(userCtr.getWxid());
		user.setUsername(userCtr.getUsername());
		user.setCampus(campusService.getId(userCtr.getCampus()));
		user.setCollege(collegeService.getId(userCtr.getCollege()));
		user.setGender(genderService.genderStrToInt(userCtr.getGender()));

		return user;
	}

	@Override
	public SmartgymUsersCtr userDaoToCtr(SmartgymUsers user) {
		SmartgymUsersCtr userCtr = new SmartgymUsersCtr();
		
		userCtr.setPhone(user.getPhone());
		userCtr.setStudentno(user.getPhone());
		userCtr.setWxid(user.getWxid());
		userCtr.setUsername(user.getUsername());
		userCtr.setCampus(campusService.getCampus(user.getCampus()));
		userCtr.setCollege(collegeService.getCollege(user.getCollege()));
		userCtr.setGender(genderService.genderIntToStr(user.getGender()));
		
		return userCtr;
	}

}
