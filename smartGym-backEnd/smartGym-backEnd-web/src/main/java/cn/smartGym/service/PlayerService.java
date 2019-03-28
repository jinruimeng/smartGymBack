package cn.smartGym.service;

import java.io.IOException;
import java.util.List;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Player;
import common.utils.SGResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

    /**
     * 根据文件名下载赛事秩序册表格 by zh
     * @param fileName
     * @return
     * @throws Exception
     */
    HSSFWorkbook getPlayersExcel(String fileName) throws Exception;

    /**
     * 根据赛事名获取秩序册文件名 by zh
     * @param game
     * @return
     */
    String getPlayersExcelFileName(String game);

    /**
     * 根据赛事名生成比赛秩序册 by zh
     * @param game
     */
    void generatePlayersExcel(String game) throws IOException;

}
