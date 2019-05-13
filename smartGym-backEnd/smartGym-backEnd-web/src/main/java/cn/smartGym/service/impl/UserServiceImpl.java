package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SgUserMapper;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojo.SgUserExample;
import cn.smartGym.pojo.SgUserExample.Criteria;
import cn.smartGym.service.UserService;
import common.enums.ErrorCode;
import common.jedis.JedisClient;
import common.utils.BloomFileter;
import common.utils.IDUtils;
import common.utils.JsonUtils;
import common.utils.SGResult;
import common.utils.BloomFileter.MisjudgmentRate;

/**
 * 用户管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SgUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Value("${BloomFileter_maxNum}")
	private int BloomFileter_maxNum;

	@Value("${BloomFileter_FPR}")
	private double BloomFileter_FPR;

	private BloomFileter fileter;

	/**
	 * 设置布隆过滤器
	 */
	public void setFileter() {
		if (fileter == null) {
			fileter = new BloomFileter(MisjudgmentRate.MIDDLE, BloomFileter_maxNum, BloomFileter_FPR);
			List<SgUser> userList = getUsersByDetailsAndStatuses(new SgUser(), 1);
			for (SgUser sgUser : userList) {
				fileter.add(sgUser.getWxId());
			}
		}
	}

	/**
	 * 注册用户/新增用户
	 * 
	 * @param userCtr
	 */
	@Override
	public SGResult register(SgUser user) {
		// 数据有效性检验
		if (StringUtils.isBlank(user.getStudentNo()) || StringUtils.isBlank(user.getName())
				|| StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getWxId()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "用户数据不完整，注册失败");

		// 1：学号 2：微信号 3：手机号
		SGResult result = checkData(user.getStudentNo(), 1);
		if (!result.isOK())
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "此学号已经被注册!");
		}

		result = checkData(user.getWxId(), 2);
		if (!result.isOK())
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "此微信已注册账号！");
		}

		result = checkData(user.getPhone(), 3);
		if (!result.isOK())
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "此手机号已经被占用!");
		}

		// 补全pojo属性
		user.setAuthority(0); // 0是普通用户
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setStatus(1); // 0是删除，1是正常
		user.setId(IDUtils.genId());

		// 把用户数据插入数据库
		userMapper.insert(user);

		// 把微信id写入布隆过滤器
		setFileter();
		fileter.add(user.getWxId());

		// 把用户信息写入redis，key：wxId value：用户信息
		jedisClient.set("wxId:" + user.getWxId(), JsonUtils.objectToJson(user));
		// 设置Session的过期时间
		jedisClient.expire("wxId:" + user.getWxId(), SESSION_EXPIRE);

		// 返回添加成功
		return SGResult.ok("注册成功！", user);
	}

	/**
	 * 删除用户：根据用户信息（微信号或学号）软删除
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public SGResult deleteUserByDtail(SgUser user) {
		if (StringUtils.isBlank(user.getWxId()) && StringUtils.isBlank(user.getStudentNo()))
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学号和微信号不能都为空!");

		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);

		if (!StringUtils.isBlank(user.getWxId()))
			criteria.andWxIdEqualTo(user.getWxId());
		if (!StringUtils.isBlank(user.getStudentNo()))
			criteria.andStudentNoEqualTo(user.getStudentNo());
		List<SgUser> userList = userMapper.selectByExample(example);

		if (userList.isEmpty())
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "没有对应的账号信息!");
		else {
			SgUser userDelete = userList.get(0);
			userDelete.setStatus(0);
			// 0-已删除 1-正常
			userDelete.setUpdated(new Date());
			userMapper.updateByPrimaryKeySelective(userDelete);

			// 删除缓存中的用户数据
			jedisClient.del(userDelete.getWxId());

			return SGResult.ok("删除账号成功！", userDelete);
		}
	}

	/**
	 * 删除用户：硬删除状态为0(status==0)的用户信息
	 * 
	 * @param
	 */
	@Override
	public void hardDeleteUser() {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		userMapper.deleteByExample(example);
	}

	/**
	 * 设置用户权限
	 * 
	 * @param authority
	 *            0-普通用户 1-院级管理员 2-校级管理员 3-开发者
	 */
	public SGResult setUserAuthority(Integer authority, String... studentNos) {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andStudentNoIn(Arrays.asList(studentNos));

		List<SgUser> userList = userMapper.selectByExample(example);
		if (userList == null || userList.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "没有查到该用户信息！");

		// 查到用户
		for (SgUser user : userList) {
			user.setAuthority(authority);
			user.setUpdated(new Date());
			userMapper.updateByPrimaryKeySelective(user);

			// 删除缓存中的用户数据
			jedisClient.del(user.getWxId());
		}
		return SGResult.ok("设置权限成功！");
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	@Override
	public SGResult update(SgUser user) {
		// 检查数据合法性
		if (user.getStudentNo() == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "学号不能为空！");
		if (user.getWxId() == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "微信号不能为空！");

		// 检查学号和微信是否对应
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentNoEqualTo(user.getStudentNo());
		criteria.andWxIdEqualTo(user.getWxId());
		criteria.andStatusEqualTo(1);
		List<SgUser> selectByExample = userMapper.selectByExample(example);
		if (selectByExample.isEmpty())
			return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "不能修改学号！");

		SgUser userOld = selectByExample.get(0);

		// 如果手机号已修改，检查新手机号是否合法
		if (!user.getPhone().equals(userOld.getPhone())) {
			if (!(boolean) checkData(user.getPhone(), 3).getData())
				return SGResult.build(ErrorCode.CONFLICT.getErrorCode(), "该手机号已经被注册！");
		}

		user.setId(userOld.getId());
		user.setUpdated(new Date());

		userMapper.updateByPrimaryKeySelective(user);

		// 删除缓存中的用户数据
		jedisClient.del(user.getWxId());

		return SGResult.ok("修改资料成功！", user);
	}

	/**
	 * 检查记录是否已存在
	 */
	@Override
	public SGResult checkData(String param, int type) {
		// 根据不同的type生成不同的查询条件
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);

		// 1：学号 2：微信号 3：手机号
		if (type == 1) {
			criteria.andStudentNoEqualTo(param);
		} else if (type == 2) {
			criteria.andWxIdEqualTo(param);
		} else if (type == 3) {
			criteria.andPhoneEqualTo(param);
		} else {
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "数据类型错误!");
		}

		// 执行查询
		List<SgUser> list = userMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (list != null && list.size() > 0) {
			// 如果有数据返回false
			return SGResult.ok(false);
		}

		// 如果没有数据返回true
		return SGResult.ok(true);
	}

	/**
	 * 查询用户：根据用户信息（微信或学号）查询记录
	 * 
	 * @param studentNo
	 */
	@Override
	public SGResult getUserByDtail(SgUser user) {
		if (user == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "请输入用户信息！");

		SgUser result = new SgUser();

		// 先去缓存中查找是否有用户信息
		String wxId = user.getWxId();
		if (!StringUtils.isBlank(wxId) && !"undefined".equals(wxId)) {
			// 去布隆过滤器中找
			setFileter();
			if (!fileter.check(wxId))
				return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查到该用户信息！");

			String userCtrSignInString = jedisClient.get(wxId);
			if (!StringUtils.isBlank(userCtrSignInString)) {
				result = JsonUtils.jsonToPojo(jedisClient.get(wxId), SgUser.class); // 取到信息后，更新过期时间
				jedisClient.expire("wxId:" + wxId, SESSION_EXPIRE);
				return SGResult.ok("查询成功!", result);
			}

			// 缓存中查不到信息，从数据库中查找
			SgUserExample example = new SgUserExample();
			if (StringUtils.isBlank(user.getWxId()) && StringUtils.isBlank(user.getStudentNo()))
				return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "微信号和学号不能都为空！");

			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo(1);

			if (!StringUtils.isBlank(user.getWxId()))
				criteria.andWxIdEqualTo(user.getWxId());
			if (!StringUtils.isBlank(user.getStudentNo()))
				criteria.andStudentNoEqualTo(user.getStudentNo());

			List<SgUser> userList = userMapper.selectByExample(example);
			if (userList == null || userList.size() <= 0)
				return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查到该用户信息！");

			result = userList.get(0);

			// 把用户信息写入redis，key：wxId value：用户信息 wxId = result.getWxId();
			jedisClient.set("wxId:" + wxId, JsonUtils.objectToJson(result));
			// 设置Session的过期时间
			jedisClient.expire("wxId:" + wxId, SESSION_EXPIRE);

			return SGResult.ok("查询成功！", result);
		}else
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查到该用户信息！");
	}

	/**
	 * 管理员根据学号查找用户信息
	 * 
	 * @param managerUser
	 *            管理员信息
	 * @param studentNoSelected
	 *            要查询的用户学号
	 */
	public SGResult getUserByManagerAndStudentNos(SgUser managerUser, String... studentNos) {
		// 得到管理员的权限级别0-普通用户 1-院级管理员 2-校级管理员 3-开发者
		Integer managerAuthority = managerUser.getAuthority();
		// 得到管理员的学院
		Integer managerCollege = managerUser.getCollege();

		// 根据学号查询结果,如果没有输入学号，则根据权限返回所有用户记录
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andAuthorityLessThan(managerAuthority);
		if (managerAuthority < 2)
			criteria.andCollegeEqualTo(managerCollege);
		if (studentNos != null && studentNos.length != 0)
			criteria.andStudentNoIn(Arrays.asList(studentNos));

		List<SgUser> userList = userMapper.selectByExample(example);
		if (userList == null || userList.size() == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查到用户信息！");

		return SGResult.ok("查询成功！", userList);
	}

	/**
	 * 查找用户列表——根据用户详细信息
	 * 
	 * @param 要查找的用户具体信息（微信号和学号）
	 */
	@Override
	public List<SgUser> getUsersByDetailsAndStatuses(SgUser user, Integer... statuses) {
		SgUserExample example = new SgUserExample();

		Criteria criteria = example.createCriteria();
		if (statuses == null || statuses.length == 0)
			criteria.andStatusNotEqualTo(0);
		else
			criteria.andStatusIn(Arrays.asList(statuses));

		if (user != null) {
			if (!StringUtils.isBlank(user.getWxId()))
				criteria.andWxIdEqualTo(user.getWxId());
			if (!StringUtils.isBlank(user.getStudentNo()))
				criteria.andStudentNoEqualTo(user.getStudentNo());
		}

		List<SgUser> userList = userMapper.selectByExample(example);

		return userList;
	}

	@Override
	public List<String> getStudentNosByUsers(List<SgUser> users) {
		ArrayList<String> studentNos = new ArrayList<>();
		for (SgUser user : users) {
			studentNos.add(user.getStudentNo());
		}
		return studentNos;
	}

}
