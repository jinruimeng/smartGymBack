package cn.smartGym.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoctr.request.SgUserCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.UserService;
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

	/**
	 * 登录时检测用户是否已注册
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/smartgym/user/signIn", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult signIn(SgUserCtr userCtr) {
		// 解密用户敏感数据
		String wxId;

		SGResult sgResult = userService.decodeUserInfo(userCtr);
		if (sgResult.getStatus() != 200)
			return sgResult;
		else
			wxId = (String) sgResult.getData();

		// 根据解析到的wxId查询用户是否注册
		SgUser user = (SgUser) userService.selectByWxId(wxId).getData();

		if (user != null)
			return SGResult.build(200, "该用户已注册！", userService.userDaoToCtr(user));
		else {
			userCtr.setWxId(wxId);
			userCtr.setStatus(0);
			return SGResult.build(200, "该用户未注册！", userCtr);
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
	public SGResult register(SgUserCtr userCtr) {
		try {
			return userService.register(userService.userCtrToDao(userCtr));
		} catch (Exception e) {
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
	public SGResult deleteUser(String wxId) {
		try {
			return userService.deleteUserByWxId(wxId);
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
	public SGResult getAllCollegesAndCampus() {
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
	public SGResult updateUser(SgUserCtr userCtr) {
		try {
			return userService.update(userService.userCtrToDao(userCtr));
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "修改资料失败！", e);
		}
	}
//
//	@RequestMapping(value = "/index", method = { RequestMethod.POST,
//			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
//	@ResponseBody
//	public String index() {
//		return "Just for test";
//	}
}
