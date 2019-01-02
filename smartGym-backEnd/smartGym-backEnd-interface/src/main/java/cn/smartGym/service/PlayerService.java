package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojo.SmartgymPlayers;
import cn.smartGym.pojoCtr.SmartgymPlayersCtr;
import common.utils.SGResult;

public interface PlayerService {

	List<SmartgymPlayersCtr> getPlayerListByStudentNo(String studentno);

	SmartgymPlayers playerCtrToDao(SmartgymPlayersCtr playerCtr);

	SmartgymPlayersCtr playerDaoToCtr(SmartgymPlayers player);
	
	SmartgymPlayers applicationDaoToplayerDao(SmartgymApplications apply);
	
	SGResult hardDeletePlayer();
	
	SGResult reviewByUniversityManager(List<SmartgymApplications> applications);
	
	SGResult genPlayerNo(List<Long> itemsId);
	
}
