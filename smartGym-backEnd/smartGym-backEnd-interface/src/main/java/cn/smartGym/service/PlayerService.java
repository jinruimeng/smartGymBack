package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojoctr.request.PlayerCtr;
import common.utils.SGResult;

public interface PlayerService {
	
	Player playerCtrToDao(PlayerCtr playerCtr);
	
	PlayerCtr playerDaoToCtr(Player player);
	
	SGResult hardDeletePlayer();

	List<Player> getPlayerListByStudentNo(String studentno);
	
	List<Player> getPlayerListByItemIdAndCollege(Long itemId, String college);
	
	List<Player> getPlayerListByItemId(Long itemId);
	
	List<Player> getPlayerListByItemDetails(Item item, String college);
	
	SGResult genPlayerNo(List<Long> itemsId);
	
	SGResult genGroupNoAndPathNo(Long itemId);

	Player applicationDaoToPlayerDao(Application apply);

	SGResult reviewByUniversityManager(List<Application> applications);

}
