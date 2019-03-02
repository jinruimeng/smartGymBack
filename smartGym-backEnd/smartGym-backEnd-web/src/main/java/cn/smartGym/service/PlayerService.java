package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Player;
import common.utils.SGResult;

public interface PlayerService {

	Player applicationDaoToPlayerDao(Application apply);

	SGResult reviewByUniversityManager(List<Application> applications);

	void hardDeletePlayer();

	void maintenancePlayer(List<Long> itemIds, List<String> studentNos);

	void genPlayerNo(List<Long> itemsId);

	SGResult genGroupNoAndPathNo(Long itemId, Integer pathNum);

	SGResult updatePlayer(Player player);

	List<Player> getPlayersByStudentNo(String studentNo);

	List<Player> getPlayersByCollegeAndItemIds(String college, Long... itemIds);

}
