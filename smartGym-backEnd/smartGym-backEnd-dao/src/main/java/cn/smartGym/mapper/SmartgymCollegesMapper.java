package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymColleges;
import cn.smartGym.pojo.SmartgymCollegesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymCollegesMapper {
    int countByExample(SmartgymCollegesExample example);

    int deleteByExample(SmartgymCollegesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmartgymColleges record);

    int insertSelective(SmartgymColleges record);

    List<SmartgymColleges> selectByExample(SmartgymCollegesExample example);

    SmartgymColleges selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmartgymColleges record, @Param("example") SmartgymCollegesExample example);

    int updateByExample(@Param("record") SmartgymColleges record, @Param("example") SmartgymCollegesExample example);

    int updateByPrimaryKeySelective(SmartgymColleges record);

    int updateByPrimaryKey(SmartgymColleges record);
}