package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
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

	@RequestMapping(value = "/user/register", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult register(SmartgymUsersCtr userCtr) {

		// 解密用户敏感数据
		SGResult sgResult = userService.decodeUserInfo(userCtr);
		if (sgResult.getStatus() != 200)
			return sgResult;
		else
			userCtr.setWxid((String) sgResult.getData());

		SmartgymUsers user = userService.userCtrToDao(userCtr);
		sgResult = userService.register(user);
		return sgResult;
	}
}
