package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.service.UserService;
import common.utils.SGResult;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
//	UserService userService = new UserServiceImpl();
	
	@RequestMapping(value="/user/register", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public SGResult register(SmartgymUsers user) {
		SGResult sgResult = userService.register(user);
		System.out.println("注册成功");
		System.out.println(user.getId());
		System.out.println(user.getPhone());
		return null;
	}
	
}
