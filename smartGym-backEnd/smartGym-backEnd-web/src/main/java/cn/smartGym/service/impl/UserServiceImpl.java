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
	private SmartgymUsersMapper smartgymUsersMapper;
	@Autowired
	private CampusService campusService;
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private GenderService genderService;

	/**
	 * Controller-Dao层接收bean转换器
	 * 
	 * @param userCtr 接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr) {
		SmartgymUsers user = new SmartgymUsers();

		user.setPhone(userCtr.getPhone());
		user.setStudentno(userCtr.getStudentno());
		user.setWxid(userCtr.getWxid());
		user.setName(userCtr.getName());
		user.setCampus(campusService.getId(userCtr.getCampus()));
		user.setCollege(collegeService.getId(userCtr.getCollege()));
		user.setGender(genderService.genderStrToInt(userCtr.getGender()));

		return user;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * 
	 * @param user 从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public SmartgymUsersCtr userDaoToCtr(SmartgymUsers user) {
		SmartgymUsersCtr userCtr = new SmartgymUsersCtr();

		userCtr.setPhone(user.getPhone());
		userCtr.setStudentno(user.getStudentno());
		userCtr.setWxid(user.getWxid());
		userCtr.setName(user.getName());
		userCtr.setCampus(campusService.getCampus(user.getCampus()));
		userCtr.setCollege(collegeService.getCollege(user.getCollege()));
		userCtr.setGender(genderService.genderIntToStr(user.getGender()));

		return userCtr;
	}

	/**
	 * 解密用户敏感数据
	 *
	 * @param encryptedData 明文,加密数据
	 * @param iv            加密算法的初始向量
	 * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code
	 *                      发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和
	 *                      session_key
	 * @return
	 */
	@Override
	public SGResult decodeUserInfo(SmartgymUsersCtr userCtr) {
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
		// 用户的唯一标识（openid）
//        String openid = (String) json.get("openid");

		/*
		 * 2、对encryptedData加密数据进行AES解密
		 */
		try {
			String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			if (null != result && result.length() > 0) {
				// 解密成功
				JSONObject userInfoJSON = JSONObject.fromObject(result);
				System.out.println(userInfoJSON.toString());
				/*
				 * Map userInfo = new HashMap(); userInfo.put("openId",
				 * userInfoJSON.get("openId")); userInfo.put("nickName",
				 * userInfoJSON.get("nickName")); userInfo.put("gender",
				 * userInfoJSON.get("gender")); userInfo.put("city", userInfoJSON.get("city"));
				 * userInfo.put("province", userInfoJSON.get("province"));
				 * userInfo.put("country", userInfoJSON.get("country"));
				 * userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
				 * userInfo.put("unionId", userInfoJSON.get("unionId"));
				 */
				System.out.println(userInfoJSON.get("unionId"));
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
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		// 1：学号 2：用户名 3：手机号
		if (type == 1) {
			criteria.andStudentnoEqualTo(param);
		} else if (type == 2) {
			criteria.andNameEqualTo(param);
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

	/**
	 * 用户注册
	 */
	@Override
	public SGResult register(SmartgymUsers user) {
		// 数据有效性检验
		if (StringUtils.isBlank(user.getStudentno()) || StringUtils.isBlank(user.getName())
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
		return SGResult.build(200, "注册成功", userDaoToCtr(user));
	}
	
	
	/**
	 * 根据微信id查询是否已注册
	 */
	@Override
	public List<SmartgymUsers> selectByWxid(String wxid) {
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		criteria.andWxidEqualTo(wxid);
		List<SmartgymUsers> result = smartgymUsersMapper.selectByExample(example);
		return result;
	}

}
