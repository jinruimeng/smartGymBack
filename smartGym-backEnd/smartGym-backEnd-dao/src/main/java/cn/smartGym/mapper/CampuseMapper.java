package cn.smartGym.mapper;

import cn.smartGym.pojo.Campuse;
import cn.smartGym.pojo.CampuseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CampuseMapper {
    long countByExample(CampuseExample example);

    int deleteByExample(CampuseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Campuse record);

    int insertSelective(Campuse record);

    List<Campuse> selectByExample(CampuseExample example);

    Campuse selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Campuse record, @Param("example") CampuseExample example);

    int updateByExample(@Param("record") Campuse record, @Param("example") CampuseExample example);

    int updateByPrimaryKeySelective(Campuse record);

    int updateByPrimaryKey(Campuse record);
}