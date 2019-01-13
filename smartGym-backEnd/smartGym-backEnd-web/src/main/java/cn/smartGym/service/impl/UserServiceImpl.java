package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SgUserMapper;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojo.SgUserExample;
import cn.smartGym.pojo.SgUserExample.Criteria;
import cn.smartGym.pojoctr.request.SgUserCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderService;
import cn.smartGym.service.UserService;
import common.utils.AesCbcUtil;
import common.utils.HttpRequest;
import common.utils.IDUtils;
import common.utils.SGResult;
import net.sf.json.JSONObject;

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
	private CampusService campusService;
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private GenderService genderService;

	/**
	 * Controller-Dao层接收bean转换器
	 * 
	 * @param userCtr
	 *            接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public SgUser userCtrToDao(SgUserCtr userCtr) {
		SgUser user = new SgUser();

		user.setPhone(userCtr.getPhone());
		user.setStudentNo(userCtr.getStudentNo());
		user.setWxId(userCtr.getWxId());
		user.setName(userCtr.getName());
		user.setCampus(campusService.getId(userCtr.getCampus()));
		user.setCollege(collegeService.getId(userCtr.getCollege()));
		user.setGender(genderService.genderStrToInt(userCtr.getGender()));

		return user;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * 
	 * @param user
	 *            从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public SgUserCtr userDaoToCtr(SgUser user) {
		SgUserCtr userCtr = new SgUserCtr();

		userCtr.setId(user.getId());
		userCtr.setPhone(user.getPhone());
		userCtr.setStudentNo(user.getStudentNo());
		userCtr.setWxId(user.getWxId());
		userCtr.setName(user.getName());
		userCtr.setCampus(campusService.getCampus(user.getCampus()));
		userCtr.setCollege(collegeService.getCollege(user.getCollege()));
		userCtr.setGender(genderService.genderIntToStr(user.getGender()));
		userCtr.setAuthority(user.getAuthority());
		userCtr.setStatus(user.getStatus());

		return userCtr;
	}

	/**
	 * 解密用户敏感数据
	 *
	 * @param encryptedData
	 *            明文,加密数据
	 * @param iv
	 *            加密算法的初始向量
	 * @param code
	 *            用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取
	 *            session_key api，将 code 换成 openid 和 session_key
	 * @return
	 */
	@Override
	public SGResult decodeUserInfo(SgUserCtr userCtr) {
		String encryptedData = userCtr.getEncryptedData();
		String code = userCtr.getCode();
		String iv = userCtr.getIv();
		// 登录凭证不能为空
		if (code == null || code.length() == 0) {
			return SGResult.build(402, "code不能为空");
		}
		// 小程序唯一标识 (在微信小程序管理后台获取)
		String wxspAppid = "wxb0c3c36ab6123dc5";
		// 小程序的 app secret (在微信小程序管理后台获取)
		String wxspSecret = "5fae01890e20ad4439657813deaf4114";
		// 授权（必填）
		String grant_type = "authorization_code";
		/*
		 * 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		 */
		// 请求参数
		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
				+ grant_type;
		// 发送请求
		String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);

		// 解析相应内容（转换成json对象）
		JSONObject json = JSONObject.fromObject(sr);
		// 获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		// 用户的唯一标识（openId）
		// String openId = (String) json.get("openid");
		// System.out.println("openId: " + openId);

		/*
		 * 2、对encryptedData加密数据进行AES解密
		 */
		try {
			String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			if (null != result && result.length() > 0) {
				// 解密成功
				JSONObject userInfoJSON = JSONObject.fromObject(result);
				/*
				 * Map userInfo = new HashMap(); userInfo.put("openId",
				 * userInfoJSON.get("openId")); userInfo.put("nickName",
				 * userInfoJSON.get("nickName")); userInfo.put("gender",
				 * userInfoJSON.get("gender")); userInfo.put("city",
				 * userInfoJSON.get("city")); userInfo.put("province",
				 * userInfoJSON.get("province")); userInfo.put("country",
				 * userInfoJSON.get("country")); userInfo.put("avatarUrl",
				 * userInfoJSON.get("avatarUrl")); userInfo.put("unionId",
				 * userInfoJSON.get("unionId"));
				 */
				// System.out.println("unionId: " +
				// userInfoJSON.get("unionId"));
				return SGResult.ok(userInfoJSON.get("unionId"));
			} else
				return SGResult.build(403, "用户信息解密失败！");
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(403, "用户信息解密失败！");
		}
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
			return SGResult.build(404, "数据类型错误!");
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
	 * 注册用户/新增用户
	 * 
	 * @param userCtr
	 */
	@Override
	public SGResult register(SgUser user) {

		// 数据有效性检验
		if (StringUtils.isBlank(user.getStudentNo()) || StringUtils.isBlank(user.getName())
				|| StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getWxId()))
			return SGResult.build(200, "用户数据不完整，注册失败");

		// 1：学号 2：微信号 3：手机号
		SGResult result = checkData(user.getStudentNo(), 1);
		if (result.getStatus() != 200)
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(200, "此学号已经被注册!");
		}

		result = checkData(user.getWxId(), 2);
		if (result.getStatus() != 200)
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(400, "此微信已注册账号！");
		}

		result = checkData(user.getPhone(), 3);
		if (result.getStatus() != 200)
			return result;
		if (!(boolean) result.getData()) {
			return SGResult.build(200, "此手机号已经被占用!");
		}

		// 补全pojo属性
		user.setAuthority(0); // 0是普通用户
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setStatus(1); // 0是删除，1是正常
		user.setId(IDUtils.genId());

		// 把用户数据插入数据库
		userMapper.insert(user);

		// 返回添加成功
		return SGResult.build(200, "注册成功！", userDaoToCtr(user));
	}

	/**
	 * 删除用户：根据微信wxId软删除
	 * 
	 * @param wxId
	 *            要删除用户的wxId
	 */
	@Override
	public SGResult deleteUserByWxId(String wxId) {
		if (StringUtils.isBlank(wxId))
			return SGResult.build(200, "微信号不能为空!");

		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andWxIdEqualTo(wxId);
		List<SgUser> list = userMapper.selectByExample(example);

		if (list.isEmpty())
			return SGResult.build(200, "没有该微信号对应的账号信息!", wxId);
		else {
			SgUser user = list.get(0);
			user.setStatus(0);
			// 0-已删除 1-正常
			user.setUpdated(new Date());
			userMapper.updateByPrimaryKeySelective(user);
			return SGResult.build(200, "删除账号成功！", user);
		}
	}

	/**
	 * 删除用户：根据学号studentNo软删除
	 * 
	 * @param studentNo
	 *            要删除用户的studentNo
	 */
	@Override
	public SGResult deleteUserByStudentNo(String studentNo) {
		if (StringUtils.isBlank(studentNo))
			return SGResult.build(200, "学号不能为空!");

		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andWxIdEqualTo(studentNo);
		List<SgUser> list = userMapper.selectByExample(example);

		if (list.isEmpty())
			return SGResult.build(200, "没有该学号对应的账号信息!", studentNo);
		else {
			SgUser user = list.get(0);
			user.setStatus(0);
			// 0-已删除 1-正常
			user.setUpdated(new Date());
			userMapper.updateByPrimaryKeySelective(user);
			return SGResult.build(200, "删除账号成功！", user);
		}
	}

	/**
	 * 删除用户：硬删除状态为0(status==0)的用户信息
	 * 
	 * @param
	 */
	@Override
	public SGResult hardDeleteUser() {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<SgUser> list = userMapper.selectByExample(example);

		for (SgUser user : list) {
			userMapper.deleteByPrimaryKey(user.getId());
		}

		return SGResult.build(200, "硬删除用户信息成功！");
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
			return SGResult.build(200, "学号不能为空，请重新登录！");
		if (user.getWxId() == null)
			return SGResult.build(200, "微信号不能为空，请重新登录！");

		// 检查学号和微信是否对应
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentNoEqualTo(user.getStudentNo());
		criteria.andWxIdEqualTo(user.getWxId());
		criteria.andStatusEqualTo(1);
		List<SgUser> selectByExample = userMapper.selectByExample(example);
		if (selectByExample.isEmpty())
			return SGResult.build(200, "不能修改学号！");

		SgUser userOld = selectByExample.get(0);

		// 如果手机号已修改，检查该手机号是否已经被注册
		if (!user.getPhone().equals(userOld.getPhone())) {
			if (!(boolean) checkData(user.getPhone(), 3).getData())
				return SGResult.build(200, "该手机号已经被注册！");
		}

		user.setId(userOld.getId());
		user.setUpdated(new Date());

		userMapper.updateByPrimaryKeySelective(user);
		return SGResult.build(200, "修改资料成功！", user);
	}

	/**
	 * 查询用户：根据微信id查询用户信息
	 * 
	 * @param wxId
	 */
	@Override
	public SGResult selectByWxId(String wxId) {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andWxIdEqualTo(wxId);
		criteria.andStatusEqualTo(1);
		List<SgUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() <= 0)
			return SGResult.build(404, "未查找到该用户！");
		
		return SGResult.build(200, "查找成功！", list.get(0));
	}

	/**
	 * 查询用户：根据微信id查询用户信息
	 * 
	 * @param studentNo
	 */
	@Override
	public SGResult selectByStudentNo(String studentNo) {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andWxIdEqualTo(studentNo);
		criteria.andStatusEqualTo(1);
		List<SgUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() <= 0)
			return SGResult.build(404, "未查找到该用户！");
		
		return SGResult.build(200, "查找成功！", list.get(0));
	}
	
//	/**
//	 * 根据学号查询所在学院
//	 */
//	@Override
//	public Integer getCollegeByStudentNo(String studentNo) {
//		SgUserExample example = new SgUserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andStudentNoEqualTo(studentNo);
//		List<SgUser> list = userMapper.selectByExample(example);
//		return list.get(0).getCollege();
//	}

	/**
	 * 管理员根据学号查找用户信息
	 * 
	 * @param managerUser
	 *            管理员信息
	 * @param studentNoSelected
	 *            要查询的用户学号
	 */
	public SGResult getUser(SgUser managerUser, String studentNoSelected) {
		// 得到管理员的权限级别0-普通用户 1-院级管理员 2-校级管理员 3-开发者
		Integer authority = managerUser.getAuthority();
		// 得到管理员的学院
		Integer college = managerUser.getCollege();

		// 根据学号查询结果
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andStudentNoEqualTo(studentNoSelected);
		List<SgUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return SGResult.build(404, "没有查到该用户，请重新输入学号！");
		// 查到用户
		SgUser user = list.get(0);

		// 如果用户的权限高于管理员，或者用户所属学院与管理员不在同一学院，则不予返回
		if (user.getAuthority() >= authority || user.getCollege() != college)
			// if(user.getAuthority() >= authority)
			return SGResult.build(404, "没有查到该用户，您权限不够！");
		return SGResult.build(200, "查询成功！", userDaoToCtr(user));
	}

	/**
	 * 设置用户权限
	 * 
	 * @param authority
	 *            0-普通用户 1-院级管理员 2-校级管理员 3-开发者
	 */
	public SGResult setUserAuthority(String studentNo, Integer authority) {
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andStudentNoEqualTo(studentNo);
		List<SgUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return SGResult.build(404, "没有查到该用户，请重新输入学号！");
		// 查到用户
		SgUser user = list.get(0);
		user.setAuthority(authority);
		userMapper.updateByPrimaryKeySelective(user);
		return SGResult.build(200, "设置权限成功！");
	}

	/**
	 * 根据前端提供的管理员信息返回User列表
	 * 
	 * @param authority
	 *            0-普通用户 1-院级管理员 2-校级管理员 3-开发者
	 */
	public List<SgUserCtr> getUserList(SgUser managerUser) {
		// 取出权限
		Integer authority = managerUser.getAuthority();
		// 查询结果集
		List<SgUser> list = new ArrayList<>();
		SgUserExample example = new SgUserExample();
		Criteria criteria = example.createCriteria();
		// 如果是开发者,则返回所有的User
		if (authority == 3) {
			criteria.andStatusEqualTo(1);
			list = userMapper.selectByExample(example);
		} else if (authority == 2) {
			// 如果是校级管理员，则返回校级管理员以下的所有User
			criteria.andStatusEqualTo(1);
			criteria.andAuthorityLessThan(2);
			list = userMapper.selectByExample(example);
		} else if (authority == 1) {
			// 如果开发者是院级管理员，则查出该院所有院级管理员以下的User
			// 得到该管理员所属的院系
			Integer college = managerUser.getCollege();
			// 根据院系和权限去查询
			criteria.andStatusEqualTo(1);
			criteria.andAuthorityLessThan(1);
			criteria.andCollegeEqualTo(college);
			list = userMapper.selectByExample(example);
		} else {
			list.add(managerUser);
		}
		// 返回给表现层
		List<SgUserCtr> listCtr = new ArrayList<>();
		for (SgUser user : list) {
			listCtr.add(userDaoToCtr(user));
		}
		return listCtr;
	}

}
