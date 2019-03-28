package cn.smartGym.service.impl;

import java.util.Arrays;
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
import common.enums.ErrorCode;
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
	private PlayerMapper playerMapper;

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
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "要审核的报名记录不能为空！");

		System.out.println(applications.size());
		for (Application application : applications) {
			Player player = applicationDaoToPlayerDao(application);
			player.setStatus(1);
			player.setCreated(new Date());
			player.setUpdated(new Date());
			playerMapper.insertSelective(player);
		}
		return SGResult.ok("校级管理员审核完成！");
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
		playerMapper.deleteByExample(example);
	}

	/**
	 * 维护参赛表
	 */
	@Override
	public void maintenancePlayer(List<Long> itemIds, List<String> studentNos) {
		PlayerExample example = new PlayerExample();

		Criteria criteriaItemIds = example.or();
		if (itemIds != null && itemIds.size() != 0)
			criteriaItemIds.andItemIdNotIn(itemIds);
		criteriaItemIds.andStatusNotEqualTo(0);

		Criteria criteriaStudentNos = example.or();
		if (studentNos != null && studentNos.size() != 0)
			criteriaStudentNos.andStudentNoNotIn(studentNos);
		criteriaStudentNos.andStatusNotEqualTo(0);

		List<Player> list = playerMapper.selectByExample(example);

		for (Player player : list) {
			player.setStatus(0);
			// 0-已删除，1-正常
			player.setUpdated(new Date());
			playerMapper.updateByPrimaryKeySelective(player);
		}
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
		List<Player> players = playerMapper.selectByExample(example);

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
			playerMapper.updateByPrimaryKeySelective(player);
		}
	}

	/**
	 * 生成组号GroupNo和赛道号PathNo——根据单个项目id
	 * 
	 * @param itemId 项目id
	 */
	public SGResult genGroupNoAndPathNo(Long itemId, Integer pathNum) {

		PlayerExample example = new PlayerExample();
		example.setOrderByClause("college");
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		criteria.andStatusEqualTo(1);
		List<Player> list = playerMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "未查询到相关项目的报名记录！");

		// 取出赛道数,如果赛道数为空或为0,默认为全部运动员分为一组
		if (pathNum == null || pathNum == 0)
			pathNum = list.size() + 1;
		//计算一共需要分多少组
		int totalGroupNum = list.size() / pathNum + 1; //20 / 6 + 1 = 4
		//计算最后一组有几人
		int numOfLastGroup = list.size() % pathNum; // 20 % 6 = 2;
		
		int i = 0;
		for(int pathNo = 1; pathNo <= numOfLastGroup; pathNo++) {
			for(int groupNo = 1; groupNo <= totalGroupNum; groupNo++) {
				Player player = list.get(i);
				player.setGroupNo(groupNo);
				player.setPathNo(pathNo);
				player.setUpdated(new Date());
				playerMapper.updateByPrimaryKeySelective(player);
				i++;
			}
		}
		
		for(int pathNo = numOfLastGroup + 1; pathNo <= pathNum; pathNo++) {
			for(int groupNo = 1; groupNo < totalGroupNum; groupNo++) {
				Player player = list.get(i);
				player.setGroupNo(groupNo);
				player.setPathNo(pathNo);
				player.setUpdated(new Date());
				playerMapper.updateByPrimaryKeySelective(player);
				i++;
			}
		}

//		// 打乱list中的item顺序
//		Collections.shuffle(list);

//		// 设置分组号和赛道号
//		for (int i = 0; i < list.size(); i++) {
//			// 设置分组号
//			list.get(i).setGroupNo(i / pathNum + 1);
//			// 设置赛道号
//			list.get(i).setPathNo(i % pathNum + 1);
//			// 更新到数据库
//			list.get(i).setUpdated(new Date());
//			playerMapper.updateByPrimaryKeySelective(list.get(i));
//		}
		return SGResult.ok("设置参赛队员分组和赛道成功！");
	}

	/**
	 * 登记比赛成绩
	 * 
	 * @param player
	 * @return
	 */
	public SGResult updatePlayer(Player player) {
		player.setUpdated(new Date());
		player.setStatus(1);
		playerMapper.updateByPrimaryKeySelective(player);
		return SGResult.ok("更新运动员信息成功！");
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
		example.setOrderByClause("updated DESC");
		List<Player> playerList = playerMapper.selectByExample(example);

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
		example.setOrderByClause("updated DESC");
		List<Player> playersList = playerMapper.selectByExample(example);

		return playersList;
	}

}
