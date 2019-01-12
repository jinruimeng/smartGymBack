package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojoCtr.PlayerCtr;
import common.utils.SGResult;

public interface PlayerService {

	List<PlayerCtr> getPlayerListByStudentNo(String studentno);

	Player playerCtrToDao(PlayerCtr playerCtr);

	PlayerCtr playerDaoToCtr(Player player);
	
	Player applicationDaoToplayerDao(Application apply);
	
	SGResult hardDeletePlayer();
	
	SGResult reviewByUniversityManager(List<Application> applications);
	
	SGResult genPlayerNo(List<Long> itemsId);
	
	SGResult genGroupNoAndPathNo(Long itemId, Integer number);
	
}
