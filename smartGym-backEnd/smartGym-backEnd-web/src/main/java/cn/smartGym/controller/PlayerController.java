package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojoCtr.SmartgymPlayersCtr;
import cn.smartGym.service.PlayerService;

@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	/**
	 * 根据学号获取已参加项目信息
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/player/getPlayerListByStudentno", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public List<SmartgymPlayersCtr> getPlayerListByStudentno(String studentno) {
		return playerService.getPlayerListByStudentno(studentno);
	}
}
