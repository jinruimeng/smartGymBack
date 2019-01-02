package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymPlayersMapper;
import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojo.SmartgymPlayers;
import cn.smartGym.pojo.SmartgymPlayersExample;
import cn.smartGym.pojo.SmartgymPlayersExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.pojoCtr.SmartgymPlayersCtr;
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
	private SmartgymPlayersMapper smartgymPlayersMapper;

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
	public List<SmartgymPlayersCtr> getPlayerListByStudentNo(String studentNo) {
		SmartgymPlayersExample example = new SmartgymPlayersExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentNoEqualTo(studentNo);
		criteria.andStatusGreaterThanOrEqualTo(1);
		List<SmartgymPlayers> list = smartgymPlayersMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		List<SmartgymPlayersCtr> result = new ArrayList<>();
		for (SmartgymPlayers player : list) {
			SmartgymPlayersCtr playerCtr = playerDaoToCtr(player);
			result.add(playerCtr);
		}
		return result;
	}

	/**
	 * playerController-Dao层接收bean转换器
	 * 
	 * @param playerCtr
	 *            接收前端数据的bean
	 * @return 封装存储到数据库中数据的bean
	 */
	@Override
	public SmartgymPlayers playerCtrToDao(SmartgymPlayersCtr playerCtr) {
		SmartgymItemsCtr itemsCtr = new SmartgymItemsCtr();
		itemsCtr.setGame(playerCtr.getGame());
		itemsCtr.setCategory(playerCtr.getCategory());
		itemsCtr.setItem(playerCtr.getItem());
		itemsCtr.setGender(playerCtr.getGender());

		// 根据项目的具体信息生成itemId
		List<Long> itemsId = itemService.getItemIdByItemDetails(itemsCtr);

		playerCtr.setItemId(itemsId.get(0));

		// 转换为Dao层的pojo
		SmartgymPlayers player = new SmartgymPlayers();
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
	 * @param player
	 *            从数据库中查询出数据封装的bean
	 * @return 返回给前端的bean
	 */
	@Override
	public SmartgymPlayersCtr playerDaoToCtr(SmartgymPlayers player) {
		// 根据项目id查询项目具体信息
		SmartgymItemsCtr itemCtr = itemService.getItemByItemId(player.getItemId());
		if (itemCtr == null)
			return null;
		// 转换为Ctr层的pojo
		SmartgymPlayersCtr playerCtr = new SmartgymPlayersCtr();
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
		SmartgymPlayersExample example = new SmartgymPlayersExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		List<SmartgymPlayers> list = smartgymPlayersMapper.selectByExample(example);

		for (SmartgymPlayers player : list) {
			smartgymPlayersMapper.deleteByPrimaryKey(player.getId());
		}

		return SGResult.build(200, "硬删除项目表成功！");
	}

	/**
	 * 根据报名表信息生成参赛信息
	 */
	@Override
	public SmartgymPlayers applicationDaoToplayerDao(SmartgymApplications apply) {
		SmartgymPlayers player = new SmartgymPlayers();

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
	public SGResult reviewByUniversityManager(List<SmartgymApplications> applications) {
		if (applications == null || applications.isEmpty())
			return SGResult.build(200, "请先选择要审核的报名记录！");

		for (SmartgymApplications apply : applications) {
			if (apply.getStatus() != 3)
				return SGResult.build(404, "校级管理员审核未通过！");
			SmartgymPlayers player = applicationDaoToplayerDao(apply);
			player.setCreated(new Date());
			player.setUpdated(new Date());
			smartgymPlayersMapper.insertSelective(player);
		}
		return SGResult.build(200, "院级管理员审核完成！");
	}

	/**
	 * 根据比赛名称设置参赛人员参赛号
	 * 
	 * @param game
	 *            最高一级的比赛名称
	 */
	@Override
	public SGResult genPlayerNo(String game) {
		// 根据比赛名称获取所有比赛小项items的id，即itemId
		List<SmartgymItemsCtr> itemCtrs = itemService.getItemsByGame(game);
		List<Long> itemIds = new ArrayList<>();
		for (SmartgymItemsCtr itemCtr : itemCtrs) {
			itemIds.add(itemCtr.getId());
		}
		// 根据item_id查出参赛表中的参赛人员的学号，保持唯一
		SmartgymPlayersExample example = new SmartgymPlayersExample();
		example.setOrderByClause("student_no");
		for (Long itemId : itemIds) {
			Criteria criteria = example.or();
			criteria.andItemIdEqualTo(itemId);
			criteria.andStatusGreaterThanOrEqualTo(1);
		}
		List<SmartgymPlayers> players = smartgymPlayersMapper.selectByExample(example);

		String preSid = "0000000";
		String prePid = "000000";
		Integer preCollege = 0;
		int index = 0;
		for (SmartgymPlayers player : players) {
			if (!player.getStudentNo().equals(preSid)) {
				if (player.getCollege() == preCollege)
					index++;
				else
					index = 1;
				preSid = player.getStudentNo();
				prePid = String.format("%02d", player.getCollege()) + String.format("%04d", index);
				preCollege = player.getCollege();
			}

			player.setPlayerNo(prePid);
			smartgymPlayersMapper.updateByPrimaryKeySelective(player);
		}

		return SGResult.build(200, "成功生成参赛号!");
	}

}
