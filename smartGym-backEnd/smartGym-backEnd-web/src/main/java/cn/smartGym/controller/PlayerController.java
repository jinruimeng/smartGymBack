package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 * 根据学号获取已参加项目信息
	 * 
	 * @param studentno
	 * @return
	 */
	@RequestMapping(value = "/smartgym/player/getPlayerListByStudentNo", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult getPlayerListByStudentNo(String studentNo) {
		try {
			List<PlayerCtr> result = playerService.getPlayerListByStudentNo(studentNo);
			if (result == null || result.size() == 0)
				return SGResult.build(200, "未报名！");
			else
				return SGResult.build(200, "查询成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			return SGResult.build(404, "查询失败！");
		}

	}
}
