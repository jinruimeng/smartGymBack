package cn.smartGym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymItemsMapper;
import cn.smartGym.mapper.SmartgymPlayersMapper;
import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojo.SmartgymItemsExample;
import cn.smartGym.pojo.SmartgymPlayers;
import cn.smartGym.pojo.SmartgymPlayersExample;
import cn.smartGym.pojo.SmartgymPlayersExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymPlayersCtr;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.JobService;
import cn.smartGym.service.PlayerService;

@Service
public class playerServiceImpl implements PlayerService {

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
	public List<SmartgymPlayersCtr> getPlayerListByStudentno(String studentno) {
		SmartgymPlayersExample example = new SmartgymPlayersExample();
		Criteria criteria = example.createCriteria();
		criteria.andStudentnoEqualTo(studentno);
		criteria.andStatusGreaterThanOrEqualTo(1);
		List<SmartgymPlayers> list = smartgymPlayersMapper.selectByExample(example);
		if (list == null || list.size() == 0)
			return null;
		List<SmartgymPlayersCtr> result = new ArrayList<>();
		for (SmartgymPlayers player : list) {
			SmartgymPlayersCtr playerCtr = playerDaotoCtr(player);
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
	public SmartgymPlayers playerCtrtoDao(SmartgymPlayersCtr playerCtr) {
		// 根据项目的具体信息生成itemId
		Long itemId = itemService.getItemIdByItemDetails(playerCtr.getGame(), playerCtr.getCategory(), playerCtr.getItem(),
				genderGroupService.genderStrToInt(playerCtr.getGender()));
		
		playerCtr.setItemId(itemId);

		// 转换为Dao层的pojo
		SmartgymPlayers player = new SmartgymPlayers();
		// 设置姓名
		player.setName(playerCtr.getName());
		// 设置学院
		player.setCollege(collegeService.getId(playerCtr.getCollege()));
		// 设置学号
		player.setStudentno(playerCtr.getStudentno());
		// 设置项目id
		player.setItemId(playerCtr.getItemId());
		// 设置性别
		player.setGender(genderGroupService.genderStrToInt(playerCtr.getGender()));
		// 设置职位
		player.setJob(jobService.jobStringToInt(playerCtr.getJob()));
		// 设置参赛号
		player.setPlayerno(playerCtr.getPlayerno());
		// 设置组别
		player.setGroup(playerCtr.getGroup());
		// 设置赛道
		player.setPath(playerCtr.getPath());
		// 设置成绩
		player.setGrades(playerCtr.getGrades());
		// 设置排名
		player.setRank(playerCtr.getRank());

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
	public SmartgymPlayersCtr playerDaotoCtr(SmartgymPlayers player) {
		// 根据项目id查询项目具体信息
		SmartgymItems item = itemService.getItemByItemId(player.getItemId());
		if (item == null)
			return null;
		// 转换为Ctr层的pojo
		SmartgymPlayersCtr playerCtr = new SmartgymPlayersCtr();
		// 设置项目信息
		playerCtr.setGame(item.getGame());
		playerCtr.setCategory(item.getCategory());
		playerCtr.setItem(item.getItem());
		playerCtr.setItemId(player.getItemId());
		// 设置姓名
		playerCtr.setName(player.getName());
		// 设置学院
		playerCtr.setCollege(collegeService.getCollege(player.getCollege()));
		// 设置学号
		playerCtr.setStudentno(player.getStudentno());
		// 设置职位
		playerCtr.setJob(jobService.jobIntToString(player.getJob()));
		// 设置参赛号
		playerCtr.setPlayerno(player.getPlayerno());
		// 设置性别
		playerCtr.setGender(genderGroupService.genderIntToStr(player.getGender()));
		// 设置组别
		playerCtr.setGroup(player.getGroup());
		// 设置赛道
		playerCtr.setPath(player.getPath());
		// 设置成绩
		playerCtr.setGrades(player.getGrades());
		// 设置排名
		playerCtr.setRank(player.getRank());

		return playerCtr;
	}
}
