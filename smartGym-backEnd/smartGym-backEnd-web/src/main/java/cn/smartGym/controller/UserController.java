package cn.smartGym.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoCtr.SgUserCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.UserService;
import cn.smartGym.utils.ConversionUtils;
import common.jedis.JedisClient;
import common.utils.JsonUtils;
import common.utils.SGResult;

/**
 * 用户管理Controller
 * 
 * @author Ruimeng Jin
 *
 */
@Controller

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CollegeService collegeService;

	@Autowired
	private CampusService campusService;

	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	/**
	 * 登录时检测用户是否已注册
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/signIn", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult signIn(SgUserCtr userCtr) throws Exception {
		// 解密用户敏感数据
		SGResult sgResult = userCtr.decodeWxId();
		String wxId = (String) sgResult.getData();

		if (!sgResult.isOK())
			return sgResult;
		else
			userCtr.setWxId(wxId);

		// 先去缓存中查找是否有用户信息
		SgUserCtr userCtrSignIn = new SgUserCtr();
		String userCtrSignInString = jedisClient.get(wxId);
		if(!StringUtils.isBlank(userCtrSignInString)) {
			userCtrSignIn = JsonUtils.jsonToPojo(jedisClient.get(wxId), SgUserCtr.class);
			return SGResult.ok("该用户已注册！", userCtrSignIn);
		}

		// 根据解析到的wxId查询用户是否注册
		SgUser user = (SgUser) userService.getUserByDtail(ConversionUtils.userCtrToDao(userCtr)).getData();

		// 如果用户已注册生成token。
		if (user != null) {
			userCtrSignIn = ConversionUtils.userDaoToCtr(user);

			// 把用户信息写入redis，key：wxId value：用户信息
			jedisClient.set("wxId:" + wxId, JsonUtils.objectToJson(userCtrSignIn));
			// 设置Session的过期时间
			jedisClient.expire("wxId:" + wxId, SESSION_EXPIRE);
			// 把结果返回
			return SGResult.ok("该用户已注册！", userCtrSignIn);
		} else {
			userCtr.setStatus(0);
			return SGResult.ok("该用户未注册！", userCtr);
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/register", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult register(SgUserCtr userCtr) throws Exception {
		SGResult sGResult = userService.register(ConversionUtils.userCtrToDao(userCtr));
		if (sGResult.isOK()) {
			SgUserCtr userCtrRegister = ConversionUtils.userDaoToCtr((SgUser) sGResult.getData());

			// 把用户信息写入redis，key：wxId value：用户信息
			jedisClient.set("wxId:" + userCtrRegister.getWxId(), JsonUtils.objectToJson(userCtrRegister));
			// 设置Session的过期时间
			jedisClient.expire("wxId:" + userCtrRegister.getWxId(), SESSION_EXPIRE);
			// 把结果返回
			return SGResult.ok("注册成功！", userCtrRegister);
		} else
			return sGResult;

	}

	/**
	 * 删除账号
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/delete", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult deleteUser(SgUserCtr userCtr) throws Exception {
		SGResult sGResult = userService.deleteUserByDtail(ConversionUtils.userCtrToDao(userCtr));
		if (sGResult.isOK()) {
			SgUserCtr userCtrDelete = ConversionUtils.userDaoToCtr((SgUser) sGResult.getData());
			jedisClient.del(userCtrDelete.getWxId());
			// 把结果返回
			return SGResult.ok("删除成功！", userCtrDelete);
		} else
			return sGResult;
	}

	/**
	 * 在注册页面获取所有的学院和校区
	 * 
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/getAllCollegesAndCampus", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getAllCollegesAndCampus() throws Exception {
		List<String> colleges = collegeService.getAllColleges();
		List<String> campuses = campusService.getAllCampuses();
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		result.put("colleges", colleges);
		result.put("campuses", campuses);
		return SGResult.ok( "获取校区和学院信息成功！", result);
	}

	/***
	 * 用户修改资料实现
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/update", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult updateUser(SgUserCtr userCtr) throws Exception {
		SGResult sGResult = userService.update(ConversionUtils.userCtrToDao(userCtr));
		if (sGResult.isOK()) {
			SgUserCtr userCtrUpdate = ConversionUtils.userDaoToCtr((SgUser) sGResult.getData());
			jedisClient.del(userCtrUpdate.getWxId());
			jedisClient.set("wxId:" + userCtrUpdate.getWxId(), JsonUtils.objectToJson(userCtrUpdate));
			jedisClient.expire("wxId:" + userCtrUpdate.getWxId(), SESSION_EXPIRE);
			// 把结果返回
			return SGResult.ok("修改资料成功！", userCtrUpdate);
		} else
			return sGResult;
	}
//
//	@RequestMapping(value = "/index", method = { RequestMethod.POST,
//			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
//	@ResponseBody
//	public String index() {
//		return "Just for test";
//	}
}
