package cn.smartGym.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Player;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.service.PlayerService;
import cn.smartGym.utils.ConversionUtils;
import common.utils.SGResult;

/**
 * 参赛人员管理Controller
 * 
 * @author ikangkang
 *
 */
@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	/**
	 * 根据学号获取报名表信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/smartgym/player/getPlayerListByStudentNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayerListByStudentNo(String studentNo) throws Exception {
		if (StringUtils.isBlank(studentNo))
			return SGResult.build(203, "学号不能为空！");

		List<Player> players = playerService.getPlayersByStudentNo(studentNo);

		if (players == null || players.size() == 0)
			return SGResult.build(201, "该学生未参加比赛！");

		List<PlayerCtr> result = ConversionUtils.playerDaoListToCtrList(players);

		return SGResult.build(200, "查询成功！", result);

	}
}
