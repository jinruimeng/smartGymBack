package cn.smartGym.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

		if (!sgResult.isOK())
			return sgResult;
		else
			userCtr.setWxId((String) sgResult.getData());

		// 根据解析到的wxId查询用户是否注册
		SgUser user = (SgUser) userService.getUserByDtail(ConversionUtils.userCtrToDao(userCtr)).getData();

		// 如果用户已注册生成token。
		if (user != null) {
			SgUserCtr userCtrResult = ConversionUtils.userDaoToCtr(user);

			String token = UUID.randomUUID().toString();
			userCtrResult.setSessionId(token);
			// 把用户信息写入redis，key：token value：用户信息
			jedisClient.set("SessionId:" + token, JsonUtils.objectToJson(userCtrResult));
			// 设置Session的过期时间
			jedisClient.expire("SessionId:" + token, SESSION_EXPIRE);
			// 6、把token返回
			return SGResult.ok("该用户已注册！", userCtrResult);
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
		try {
			return userService.register(ConversionUtils.userCtrToDao(userCtr));
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "注册失败!", e);
		}

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
		try {
			return userService.deleteUserByDtail(ConversionUtils.userCtrToDao(userCtr));
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "删除账号失败！", e);
		}
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
		return SGResult.build(200, "获取校区和学院信息成功！", result);
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
		try {
			return userService.update(ConversionUtils.userCtrToDao(userCtr));
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "修改资料失败！", e);
		}
	}

}
