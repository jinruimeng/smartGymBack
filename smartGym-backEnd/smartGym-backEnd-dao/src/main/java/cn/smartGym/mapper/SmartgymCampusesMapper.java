package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymCampuses;
import cn.smartGym.pojo.SmartgymCampusesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymCampusesMapper {
    int countByExample(SmartgymCampusesExample example);

    int deleteByExample(SmartgymCampusesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmartgymCampuses record);

    int insertSelective(SmartgymCampuses record);

    List<SmartgymCampuses> selectByExample(SmartgymCampusesExample example);

    SmartgymCampuses selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmartgymCampuses record, @Param("example") SmartgymCampusesExample example);

    int updateByExample(@Param("record") SmartgymCampuses record, @Param("example") SmartgymCampusesExample example);

    int updateByPrimaryKeySelective(SmartgymCampuses record);

    int updateByPrimaryKey(SmartgymCampuses record);
}