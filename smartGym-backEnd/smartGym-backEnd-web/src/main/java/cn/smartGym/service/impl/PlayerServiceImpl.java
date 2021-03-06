package cn.smartGym.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.PlayerMapper;
import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojo.PlayerExample;
import cn.smartGym.pojo.PlayerExample.Criteria;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.PlayerService;
import cn.smartGym.utils.CollegeAndCampusUtils;
import common.enums.ErrorCode;
import common.enums.Gender;
import common.enums.GenderGroup;
import common.enums.Job;
import common.utils.ExcelHelper;
import common.utils.SGResult;

/**
 * 参赛人员管理Service
 *
 * @author Ruimeng Jin
 */
@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private ItemService itemService;

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

	public SGResult updateUser(SgUser userUpdate) {
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();

		if (StringUtils.isNotBlank(userUpdate.getStudentNo()))
			criteria.andStudentNoEqualTo(userUpdate.getStudentNo());

		Player playerExample = new Player();
		playerExample.setName(userUpdate.getName());
		playerExample.setCollege(userUpdate.getCollege());
		playerExample.setUpdated(new Date());
		playerMapper.updateByExampleSelective(playerExample, example);

		return SGResult.ok();
	}

	/**
	 * 生成参赛号PlayerNo——根据itemIds
	 *
	 * @param itemIds 项目的id列表
	 */
	@Override
	public void genPlayerNo(List<Long> itemIds) {
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("college,student_no");
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
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "相关项目无报名人员！");

		// 取出赛道数,如果赛道数为空或为0,默认为全部运动员分为一组
		if (pathNum == null || pathNum == 0)
			pathNum = list.size() + 1;
		// 计算一共需要分多少组
		int totalGroupNum = list.size() / pathNum + 1; // 26 / 8 + 1 = 4
		// 计算最后一组有几人
		int numOfLastGroup = list.size() % pathNum; // 26 % 8 = 2;

		int index = 0;
		for (int pathNo = 1; pathNo <= numOfLastGroup; pathNo++) {
			for (int groupNo = 1; groupNo <= totalGroupNum; groupNo++) {
				Player player = list.get(index);
				player.setGroupNo(groupNo);
				player.setPathNo(pathNo);
				player.setUpdated(new Date());
				playerMapper.updateByPrimaryKeySelective(player);
				index++;
			}
		}

		for (int pathNo = numOfLastGroup + 1; pathNo <= pathNum; pathNo++) {
			for (int groupNo = 1; groupNo < totalGroupNum; groupNo++) {
				Player player = list.get(index);
				player.setGroupNo(groupNo);
				player.setPathNo(pathNo);
				player.setUpdated(new Date());
				playerMapper.updateByPrimaryKeySelective(player);
				index++;
			}
		}

		// 判断最后一组的人数，如果人数少于3人，则需要从倒数第二组取人过来补到4人
		if (numOfLastGroup < 3) {
			int num = 4 - numOfLastGroup; // 4表示补到4人
			for (int j = 0; j < num; j++) {
				Player player = list.get(index - 1 - (totalGroupNum - 1) * j);
				player.setGroupNo(totalGroupNum);
				player.setPathNo(numOfLastGroup + j + 1);
				player.setUpdated(new Date());
				playerMapper.updateByPrimaryKeySelective(player);
			}
		}

		return SGResult.ok("设置参赛队员分组和赛道成功！");
	}

	/**
	 * 登记比赛成绩
	 *
	 * @param player
	 * @return
	 */
	public SGResult registerGrades(Player player, Integer type, Integer type2) {
		// 重新拼装成绩，时间类成绩0|00:00:00:00，长度类成绩1|00.000
		String grades = player.getGrades().trim();
		if (type == 0) {
			if (!grades.matches("^[0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{2}$"))
				return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "输入成绩格式非法");
		} else if (type == 1) {
			if (!grades.matches("^[0-9]{2}\\.{1}[0-9]{3}$"))
				return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "输入成绩格式非法");
		} else {
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "输入成绩类型非法");
		}
		if (type == null || type == 0)
			player.setGrades(type + "|" + grades);
		else
			player.setFinalGrades(type + "|" + grades);
		player.setUpdated(new Date());
		player.setStatus(1);
		playerMapper.updateByPrimaryKeySelective(player);

		genRank(playerMapper.selectByPrimaryKey(player.getId()).getItemId(), type);

		return SGResult.ok("登记比赛成绩成功！");
	}

	/**
	 * 生成排名
	 */
	public SGResult genRank(Long itemId, Integer type) {
		PlayerExample example0 = new PlayerExample();
		if (playerMapper.countByExample(example0) == 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "相关项目无参赛人员！");

		// 先取出决赛成绩不为空的
		PlayerExample example1 = new PlayerExample();
		Criteria criteria1 = example1.createCriteria();
		criteria1.andItemIdEqualTo(itemId);
		criteria1.andStatusEqualTo(1);
		criteria1.andFinalGradesIsNotNull();
		criteria1.andFinalGradesNotEqualTo("0|00:00:00:00");
		criteria1.andFinalGradesNotEqualTo("1|00.000");

		if (type == 0) {
			example1.setOrderByClause("final_grades,grades");
		} else if (type == 1) {
			example1.setOrderByClause("final_grades DESC,grades DESC");
		}
		List<Player> playerList1 = playerMapper.selectByExample(example1);

		// 取出决赛为空，但预赛成绩不为空的
		PlayerExample example2 = new PlayerExample();
		Criteria criteria21 = example2.or();
		criteria21.andItemIdEqualTo(itemId);
		criteria21.andStatusEqualTo(1);
		criteria21.andFinalGradesIsNull();
		criteria21.andGradesIsNotNull();
		criteria21.andGradesNotEqualTo("0|00:00:00:00");
		criteria21.andGradesNotEqualTo("1|00.000");

		Criteria criteria22 = example2.or();
		criteria22.andItemIdEqualTo(itemId);
		criteria22.andStatusEqualTo(1);
		criteria22.andFinalGradesEqualTo("0|00:00:00:00");
		criteria22.andGradesIsNotNull();
		criteria22.andGradesNotEqualTo("0|00:00:00:00");
		criteria22.andGradesNotEqualTo("1|00.000");

		Criteria criteria23 = example2.or();
		criteria23.andItemIdEqualTo(itemId);
		criteria23.andStatusEqualTo(1);
		criteria23.andFinalGradesEqualTo("1|00.000");
		criteria23.andGradesIsNotNull();
		criteria23.andGradesNotEqualTo("0|00:00:00:00");
		criteria23.andGradesNotEqualTo("1|00.000");

		if (type == 0) {
			example2.setOrderByClause("grades");
		} else if (type == 1) {
			example2.setOrderByClause("grades DESC");
		}
		List<Player> playerList2 = playerMapper.selectByExample(example2);

		Integer index = 1;
		Integer tmpRank = 1;
		String grades = null;
		for (Player player : playerList1) {
			if (player.getFinalGrades().equals(grades))
				player.setRankNo(tmpRank);
			else {
				player.setRankNo(index);
				tmpRank = index;
			}
			grades = player.getFinalGrades();
			index++;
			player.setUpdated(new Date());
			playerMapper.updateByPrimaryKeySelective(player);
		}
		grades = null;

		for (Player player : playerList2) {
			if (player.getGrades().equals(grades))
				player.setRankNo(tmpRank);
			else {
				player.setRankNo(index);
				tmpRank = index;
			}
			grades = player.getGrades();
			index++;
			player.setUpdated(new Date());
			playerMapper.updateByPrimaryKeySelective(player);
		}
		return SGResult.ok("生成排名成功！");
	}

	/**
	 * 取出排名topK
	 */
	public SGResult getTopK(Long itemId, Integer k) {
		PlayerExample example = new PlayerExample();
		example.setOrderByClause("rank_no");
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		criteria.andStatusEqualTo(1);
		criteria.andRankNoIsNotNull();
		criteria.andRankNoLessThanOrEqualTo(k);
		List<Player> list = playerMapper.selectByExample(example);
		if (list == null || list.size() <= 0)
			return SGResult.build(ErrorCode.NO_CONTENT.getErrorCode(), "相关项目无参赛人员！");
		return SGResult.ok(list);

//		// 对总记录list取前八（可能同时有多个第八）
//		// list长度不足8,k=8
//		if (list.size() <= k) {
//			return SGResult.ok(list);
//		}
//		// list长度大于8
//		else {
//			List<Player> res = new ArrayList<>();
//			// 设置前8
//			for (int i = 0; i < k; i++)
//				res.add(list.get(i));
//			// 如果从第8名开始成绩相同
//			int j = 8;
//			while (j < list.size()) {
//				if (list.get(j).getRankNo() == list.get(j - 1).getRankNo())
//					res.add(list.get(j));
//				else
//					break;
//				j++;
//			}
//			return SGResult.ok(res);
//		}
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
		if (college != null && !college.equals("total")) {
			criteria.andCollegeEqualTo(CollegeAndCampusUtils.getCollegeIndex(college));
		}

		if (itemIds != null && itemIds.length != 0)
			criteria.andItemIdIn(Arrays.asList(itemIds));
		example.setOrderByClause("group_no,path_no,student_no,updated DESC");
		List<Player> playersList = playerMapper.selectByExample(example);

		return playersList;
	}

	@Override
	public String getPlayersExcelFilePath(String game) {
		String fileName = String.valueOf(game.hashCode());
		String filePath = "downloadFiles/" + fileName + ".xls";
		return filePath;
	}

	@Override
	public void generatePlayersExcel(String game) throws IOException {
		// 构建Excel文件路径
		String filePath = getPlayersExcelFilePath(game);
		// 获取Excel标题行数据
		List<String> title = Arrays.asList(ExcelHelper.playersExcelTitleArray);
		// 获取playersData
		Item item = new Item();
		item.setGame(game);
		List<Item> items = itemService.getItemsByDetailsAndStatuses(item, 2, 3);
		List<Long> ids = itemService.getItemIdsByItems(items);
		List<Player> players = getPlayersByCollegeAndItemIds("total", ids.toArray(new Long[ids.size()]));
		List<List<String>> playersData = new ArrayList<>();
		for (Player player : players) {
			playersData.add(convertPlayerToRowData(player));
		}
		ExcelHelper.writeExcel(title, playersData, filePath);

	}

	/**
	 * 将player对象转化为秩序册EXCEL行数据 by zh
	 *
	 * @param player
	 * @return
	 */
	private List<String> convertPlayerToRowData(Player player) {
		List<String> data = new ArrayList<>();
		data.add(player.getPlayerNo());
		data.add(player.getStudentNo());
		data.add(player.getName());
		data.add(Gender.getName(player.getGender()));
		data.add(collegeService.getCollege(player.getCollege()));
		Item item = itemService.getItemByItemIdAndStatuses(player.getItemId());
		data.add(item.getGame());
		data.add(item.getCategory());
		data.add(item.getItem());
		data.add(GenderGroup.getName(item.getGender()));
		data.add(Job.getName(player.getJob()));
		data.add(String.valueOf(player.getGroupNo()));
		data.add(String.valueOf(player.getPathNo()));
		return data;
	}

	////////////// 备用////////////////
	@Override
	public void generatePlayersDetailedExcel(String game) throws IOException {
		// 构建Excel文件路径
		String filePath = getPlayersExcelFilePath(game);
		// 获取Excel标题行数据
		List<String> title = Arrays.asList(ExcelHelper.playersDetailedExcelTitleArray);
		// 获取playersData
		Item item = new Item();
		item.setGame(game);
		List<Item> items = itemService.getItemsByDetailsAndStatuses(item, 2, 3);
		List<Long> ids = itemService.getItemIdsByItems(items);
		List<Player> players = getPlayersByCollegeAndItemIds("total", ids.toArray(new Long[ids.size()]));
		List<List<String>> playersData = new ArrayList<>();
		for (Player player : players) {
			playersData.add(convertPlayerDetailedToRowData(player));
		}
		ExcelHelper.writeExcel(title, playersData, filePath);

	}

	/**
	 * 将player对象转化为秩序册EXCEL行数据 by zh
	 *
	 * @param player
	 * @return
	 */
	private List<String> convertPlayerDetailedToRowData(Player player) {
		List<String> data = new ArrayList<>();
		data.add(player.getPlayerNo());
		data.add(player.getStudentNo());
		data.add(player.getName());
		data.add(Gender.getName(player.getGender()));
		data.add(collegeService.getCollege(player.getCollege()));
		Item item = itemService.getItemByItemIdAndStatuses(player.getItemId());
		data.add(item.getGame());
		data.add(item.getCategory());
		data.add(item.getItem());
		data.add(GenderGroup.getName(item.getGender()));
		data.add(Job.getName(player.getJob()));
		data.add(String.valueOf(player.getGroupNo()));
		data.add(String.valueOf(player.getPathNo()));
		data.add(String.valueOf(player.getGrades()));
		data.add(String.valueOf(player.getFinalGrades()));
		data.add(String.valueOf(player.getRankNo()));
		// data.add(String.valueOf(player.getDescription()));
		return data;
	}

	@Override
	public Player getPlayerByPlayerIdAndStatuses(Long playerId, Integer... statuses) {
		// 根据项目id和状态status查询比赛项目信息
		PlayerExample example = new PlayerExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(playerId);
		if (statuses == null || statuses.length == 0 || !Arrays.asList(statuses).contains(0))
			criteria.andStatusNotEqualTo(0);
		if (statuses != null && statuses.length != 0)
			criteria.andStatusIn(Arrays.asList(statuses));

		List<Player> playerList = playerMapper.selectByExample(example);
		if (playerList == null || playerList.size() <= 0)
			return null;
		else
			return playerList.get(0);
	}

}
