package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.PlayerMapper;
import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojo.PlayerExample;
import cn.smartGym.pojo.PlayerExample.Criteria;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.JobService;
import cn.smartGym.service.PlayerService;
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

	@Autowired
	private CollegeService collegeService;

	@Autowired
	private GenderGroupService genderGroupService;

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemService itemService;

	/**
	 * 根据学号获取已参加项目信息
	 * 
	 * @param studentno
	 * @return
	 */
	public List<PlayerCtr> getPlayerListByStudentNo(String studentNo) {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentNoEqualTo(studentNo);
		criteria.andStatusGreaterThanOrEqualTo(1);
		List<Player> list = PlayerMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		List<PlayerCtr> result = new ArrayList<>();
		for (Player player : list) {
			PlayerCtr playerCtr = playerDaoToCtr(player);
			result.add(playerCtr);
		}
		return result;
	}

	/**
	 * playerController-Dao层接收bean转换器
	 * 
	 * @param playerCtr 接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public Player playerCtrToDao(PlayerCtr playerCtr) {
		ItemCtr itemsCtr = new ItemCtr();
		itemsCtr.setGame(playerCtr.getGame());
		itemsCtr.setCategory(playerCtr.getCategory());
		itemsCtr.setItem(playerCtr.getItem());
		itemsCtr.setGender(playerCtr.getGender());

		// 根据项目的具体信息生成itemId
		List<Long> itemsId = itemService.getItemIdByItemDetails(itemsCtr);

		playerCtr.setItemId(itemsId.get(0));

		// 转换为Dao层的pojo
		Player player = new Player();
		// 设置姓名
		player.setName(playerCtr.getName());
		// 设置学院
		player.setCollege(collegeService.getId(playerCtr.getCollege()));
		// 设置学号
		player.setStudentNo(playerCtr.getStudentNo());
		// 设置项目id
		player.setItemId(playerCtr.getItemId());
		// 设置性别
		player.setGender(genderGroupService.genderStrToInt(playerCtr.getGender()));
		// 设置职位
		player.setJob(jobService.jobStringToInt(playerCtr.getJob()));
		// 设置参赛号
		player.setPlayerNo(playerCtr.getPlayerNo());
		// 设置组别
		player.setGroupNo(playerCtr.getGroupNo());
		// 设置赛道
		player.setPathNo(playerCtr.getPathNo());
		// 设置成绩
		player.setGrades(playerCtr.getGrades());
		// 设置排名
		player.setRankNo(playerCtr.getRankNo());

		return player;
	}

	/**
	 * Dao-Controller层接收bean转换器
	 * 
	 * @param player 从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public PlayerCtr playerDaoToCtr(Player player) {
		// 根据项目id查询项目具体信息
		ItemCtr itemCtr = itemService.getItemByItemId(player.getItemId(), null);
		if (itemCtr == null)
			return null;
		// 转换为Ctr层的pojo
		PlayerCtr playerCtr = new PlayerCtr();
		// 设置项目信息
		playerCtr.setGame(itemCtr.getGame());
		playerCtr.setCategory(itemCtr.getCategory());
		playerCtr.setItem(itemCtr.getItem());
		playerCtr.setItemId(player.getItemId());
		// 设置姓名
		playerCtr.setName(player.getName());
		// 设置学院
		playerCtr.setCollege(collegeService.getCollege(player.getCollege()));
		// 设置学号
		playerCtr.setStudentNo(player.getStudentNo());
		// 设置职位
		playerCtr.setJob(jobService.jobIntToString(player.getJob()));
		// 设置参赛号
		playerCtr.setPlayerNo(player.getPlayerNo());
		// 设置性别
		playerCtr.setGender(genderGroupService.genderIntToStr(player.getGender()));
		// 设置组别
		playerCtr.setGroupNo(player.getGroupNo());
		// 设置赛道
		playerCtr.setPathNo(player.getPathNo());
		// 设置成绩
		playerCtr.setGrades(player.getGrades());
		// 设置排名
		playerCtr.setRankNo(player.getRankNo());

		return playerCtr;
	}

	/**
	 * 硬删除状态为（0）的选手信息
	 */
	@Override
	public SGResult hardDeletePlayer() {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<Player> list = PlayerMapper.selectByExample(example);

		for (Player player : list) {
			PlayerMapper.deleteByPrimaryKey(player.getId());
		}

		return SGResult.build(200, "硬删除项目表成功！");
	}

	/**
	 * 根据报名表信息生成参赛信息
	 */
	@Override
	public Player applicationDaoToplayerDao(Application apply) {
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
			return SGResult.build(200, "请先选择要审核的报名记录！");

		for (Application apply : applications) {
			if (apply.getStatus() != 3)
				return SGResult.build(404, "校级管理员审核未通过！");
			Player player = applicationDaoToplayerDao(apply);
			player.setCreated(new Date());
			player.setUpdated(new Date());
			PlayerMapper.insertSelective(player);
		}
		return SGResult.build(200, "院级管理员审核完成！");
	}

	/**
	 * 设置参赛队员参赛号
	 */
	@Override
	public SGResult genPlayerNo(List<Long> itemsId) {
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("student_no");
		for (Long itemId : itemsId) {
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
		return SGResult.build(200, "生成参赛号成功!");
	}


	/**
	 * 产生参赛队员的组号和赛道号，每个项目的分组都不同，所以要传入需要设置分组的项目id
	 */

	public SGResult genGroupNoAndPathNo(Long itemId, Integer number) {
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("student_no");
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		criteria.andStatusGreaterThanOrEqualTo(1);
		List<Player> list = PlayerMapper.selectByExample(example);
		if(list == null || list.size() <= 0)
			return SGResult.build(404, "设置参赛队员分组和赛道失败！");

		//设置分组号和赛道号
		for(int i = 0; i < list.size(); i++) {
			//设置分组号
			list.get(i).setGroupNo(i / number + 1);
			//设置赛道号
			list.get(i).setPathNo(i % number + 1);
			//更新到数据库
			PlayerMapper.updateByPrimaryKeySelective(list.get(i));
		}
		return SGResult.build(200, "设置参赛队员分组和赛道成功！");
	}
	
}
