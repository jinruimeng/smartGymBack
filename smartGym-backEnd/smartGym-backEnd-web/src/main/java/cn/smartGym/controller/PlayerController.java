package cn.smartGym.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojoctr.request.PlayerCtr;
import cn.smartGym.service.PlayerService;
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
	public SGResult getPlayerListByStudentNo(String studentNo) {
		try {
			List<Player> list = playerService.getPlayerListByStudentNo(studentNo);
			if (list == null || list.size() == 0)
				return SGResult.build(200, "该学生未参加比赛！");
			List<PlayerCtr> result = new ArrayList<>();
			for (Player player : list) {
				PlayerCtr playerCtr = playerService.playerDaoToCtr(player);
				result.add(playerCtr);
			}
			return SGResult.build(200, "查询成功！", result);
			
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}

	}
	
	/**
	 * 根据项目详情和学院获取报名表信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/smartgym/player/getPlayerListByItemDetailsAndCollege", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayerListByItemDetailsAndCollege(Item item, String college) {
		try {
			List<Player> list = playerService.getPlayerListByItemDetails(item, college);
			if (list == null || list.size() == 0)
				return SGResult.build(200, "未查到相关信息！");
			List<PlayerCtr> result = new ArrayList<>();
			for (Player player : list) {
				PlayerCtr playerCtr = playerService.playerDaoToCtr(player);
				result.add(playerCtr);
			}
			return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！", e);
		}
	}
}
