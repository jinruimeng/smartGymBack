package cn.smartGym.service;

import java.util.List;

import cn.smartGym.pojo.SmartgymPlayers;
import cn.smartGym.pojoCtr.SmartgymPlayersCtr;

public interface PlayerService {

	List<SmartgymPlayersCtr> getPlayerListByStudentNo(String studentno);

	SmartgymPlayers playerCtrtoDao(SmartgymPlayersCtr playerCtr);

	SmartgymPlayersCtr playerDaotoCtr(SmartgymPlayers player);
}
