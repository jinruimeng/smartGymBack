package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymPlayers;
import cn.smartGym.pojo.SmartgymPlayersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymPlayersMapper {
    int countByExample(SmartgymPlayersExample example);

    int deleteByExample(SmartgymPlayersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymPlayers record);

    int insertSelective(SmartgymPlayers record);

    List<SmartgymPlayers> selectByExample(SmartgymPlayersExample example);

    SmartgymPlayers selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymPlayers record, @Param("example") SmartgymPlayersExample example);

    int updateByExample(@Param("record") SmartgymPlayers record, @Param("example") SmartgymPlayersExample example);

    int updateByPrimaryKeySelective(SmartgymPlayers record);

    int updateByPrimaryKey(SmartgymPlayers record);
}