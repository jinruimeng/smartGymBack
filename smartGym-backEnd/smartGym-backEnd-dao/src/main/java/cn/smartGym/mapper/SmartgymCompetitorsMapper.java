package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymCompetitors;
import cn.smartGym.pojo.SmartgymCompetitorsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymCompetitorsMapper {
    long countByExample(SmartgymCompetitorsExample example);

    int deleteByExample(SmartgymCompetitorsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymCompetitors record);

    int insertSelective(SmartgymCompetitors record);

    List<SmartgymCompetitors> selectByExample(SmartgymCompetitorsExample example);

    SmartgymCompetitors selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymCompetitors record, @Param("example") SmartgymCompetitorsExample example);

    int updateByExample(@Param("record") SmartgymCompetitors record, @Param("example") SmartgymCompetitorsExample example);

    int updateByPrimaryKeySelective(SmartgymCompetitors record);

    int updateByPrimaryKey(SmartgymCompetitors record);
}