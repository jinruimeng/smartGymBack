package cn.smartGym.mapper;

import cn.smartGym.pojo.Campus;
import cn.smartGym.pojo.CampusExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CampusMapper {
    long countByExample(CampusExample example);

    int deleteByExample(CampusExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Campus record);

    int insertSelective(Campus record);

    List<Campus> selectByExample(CampusExample example);

    Campus selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Campus record, @Param("example") CampusExample example);

    int updateByExample(@Param("record") Campus record, @Param("example") CampusExample example);

    int updateByPrimaryKeySelective(Campus record);

    int updateByPrimaryKey(Campus record);
}