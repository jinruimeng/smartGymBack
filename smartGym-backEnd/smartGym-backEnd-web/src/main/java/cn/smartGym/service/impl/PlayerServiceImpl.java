package cn.smartGym.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.PlayerMapper;
import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojo.PlayerExample;
import cn.smartGym.pojo.PlayerExample.Criteria;
import cn.smartGym.service.PlayerService;
import cn.smartGym.utils.CollegeAndCampusUtils;
import common.utils.SGResult;

/**
 * 参赛人员管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerMapper PlayerMapper;

	/**
	 * 根据报名表信息生成参赛信息
	 * 
	 * @param apply 报名表信息
	 */
	@Override
	public Player applicationDaoToPlayerDao(Application apply) {
		Player player = new Player();
		player.setId(apply.getId());
		player.setName(apply.getName());
		player.setCollege(apply.getCollege());
		player.setStudentNo(apply.getStudentNo());
		player.setItemId(apply.getItemId());
		player.setJob(apply.getJob());
		player.setGender(apply.getGender());

		return player;
	}

	/**
	 * 校级管理员审核通过
	 */
	@Override
	public SGResult reviewByUniversityManager(List<Application> applications) {
		if (applications == null || applications.isEmpty())
			return SGResult.build(203, "请先选择要审核的报名记录！");

		System.out.println(applications.size());
		for (Application application : applications) {
			Player player = applicationDaoToPlayerDao(application);
			player.setStatus(1);
			player.setCreated(new Date());
			player.setUpdated(new Date());
			PlayerMapper.insertSelective(player);
		}
		return SGResult.build(200, "校级管理员审核完成！");
	}

	/**
	 * 硬删除状态为（0）的选手信息
	 * 
	 */
	@Override
	public void hardDeletePlayer() {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		PlayerMapper.deleteByExample(example);
	}

	/**
	 * 生成参赛号PlayerNo——根据itemIds
	 * 
	 * @param itemIds 项目的id列表
	 */
	@Override
	public void genPlayerNo(List<Long> itemIds) {
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("student_no");
		for (Long itemId : itemIds) {
			Criteria criteria = example.or();
			criteria.andItemIdEqualTo(itemId);
			criteria.andStatusGreaterThanOrEqualTo(1);
		}
		List<Player> players = PlayerMapper.selectByExample(example);

		String curSid = "0000000";
		String curPid = "000000";
		Integer curCollege = 0;
		int index = 0;
		for (Player player : players) {
			if (!player.getStudentNo().equals(curSid)) {
				if (player.getCollege() == curCollege)
					index++;
				else
					index = 1;
				curSid = player.getStudentNo();
				curPid = String.format("%02d", player.getCollege()) + String.format("%04d", index);
				curCollege = player.getCollege();
			}

			player.setPlayerNo(curPid);
			player.setUpdated(new Date());
			PlayerMapper.updateByPrimaryKeySelective(player);
		}
	}

	/**
	 * 生成组号GroupNo和赛道号PathNo——根据单个项目id
	 * 
	 * @param itemId 项目id
	 */
	public SGResult genGroupNoAndPathNo(Long itemId, Integer pathNum) {

		PlayerExample example = new PlayerExample();
		example.setOrderByClause("id");
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		criteria.andStatusEqualTo(1);
		List<Player> list = PlayerMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return SGResult.build(404, "设置参赛队员分组和赛道失败！");

		// 打乱list中的item顺序
		Collections.shuffle(list);

		// 取出赛道数,如果赛道数为空或为0,默认为全部运动员分为一组
		if (pathNum == null || pathNum == 0)
			pathNum = list.size() + 1;

		// 设置分组号和赛道号
		for (int i = 0; i < list.size(); i++) {
			// 设置分组号
			list.get(i).setGroupNo(i / pathNum + 1);
			// 设置赛道号
			list.get(i).setPathNo(i % pathNum + 1);
			// 更新到数据库
			list.get(i).setUpdated(new Date());
			PlayerMapper.updateByPrimaryKeySelective(list.get(i));
		}
		return SGResult.build(200, "设置参赛队员分组和赛道成功！");
	}

	/**
	 * 根据学号获取参赛记录
	 * 
	 * @param studentno
	 * @return
	 */
	public List<Player> getPlayersByStudentNo(String studentNo) {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(studentNo))
			criteria.andStudentNoEqualTo(studentNo);
		criteria.andStatusNotEqualTo(0);
		List<Player> playerList = PlayerMapper.selectByExample(example);

		return playerList;
	}

	/**
	 * 根据学院college和项目id获取参赛记录
	 * 
	 * @param         college(为"total"时，查询所有学院的参赛记录)
	 * @param itemIds
	 * @return
	 */
	public List<Player> getPlayersByCollegeAndItemIds(String college, Long... itemIds) {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(0);
		if (!college.equals("total")) {
			criteria.andCollegeEqualTo(CollegeAndCampusUtils.getCollegeIndex(college));
		}

		if (itemIds != null && itemIds.length != 0)
			criteria.andItemIdIn(Arrays.asList(itemIds));
		List<Player> playersList = PlayerMapper.selectByExample(example);

		return playersList;
	}

}
