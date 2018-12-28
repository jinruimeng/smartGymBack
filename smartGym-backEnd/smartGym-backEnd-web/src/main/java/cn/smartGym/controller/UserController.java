package cn.smartGym.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
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
	@RequestMapping(value = "/user/signin", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult signIn(SmartgymUsersCtr userCtr) {
		// 解密用户敏感数据
		String wxid;

		SGResult sgResult = userService.decodeUserInfo(userCtr);
		if (sgResult.getStatus() != 200)
			return sgResult;
		else
			wxid = (String) sgResult.getData();

		// 根据解析到的wxid查询用户是否注册
		List<SmartgymUsers> result = userService.selectByWxid(wxid);

		if (!result.isEmpty())
			return SGResult.build(200, "该用户已注册！", userService.userDaoToCtr(result.get(0)));
		else
			return SGResult.build(200, "该用户未注册！", wxid);
	}

	/**
	 * 用户注册实现
	 * 
	 * @param userCtr
	 * @return
	 */
	@RequestMapping(value = "/user/register", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult register(SmartgymUsersCtr userCtr) {
		SmartgymUsers user = userService.userCtrToDao(userCtr);
		return userService.register(user);
	}

	/**
	 * 在注册页面获取所有的学院和校区
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/getAllCollegesAndCampus", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getAllCollegesAndCampus() {
		List<String> colleges = collegeService.getAllcolleges();
		List<String> campuses = campusService.getAllCampuses();
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		result.put("colleges", colleges);
		result.put("campuses", campuses);
		return SGResult.build(200, "获取校区和学院信息成功！", result);
	}
}
