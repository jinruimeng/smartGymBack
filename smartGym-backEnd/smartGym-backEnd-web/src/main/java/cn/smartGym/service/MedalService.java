package cn.smartGym.service;

import java.util.List;
import cn.smartGym.pojo.Medal;
import common.utils.SGResult;

public interface MedalService {
	SGResult updateByGame(String game);

	SGResult updateByGameAndCollege(String game, Integer college);

	void hardDeleteMedal();

	void maintenanceMedal();

	List<Medal> getMedalsByDetails(Medal medal);

}
