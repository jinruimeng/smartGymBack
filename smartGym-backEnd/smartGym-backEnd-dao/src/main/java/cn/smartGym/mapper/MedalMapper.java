package cn.smartGym.mapper;

import cn.smartGym.pojo.Medal;
import cn.smartGym.pojo.MedalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MedalMapper {
    long countByExample(MedalExample example);

    int deleteByExample(MedalExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Medal record);

    int insertSelective(Medal record);

    List<Medal> selectByExample(MedalExample example);

    Medal selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Medal record, @Param("example") MedalExample example);

    int updateByExample(@Param("record") Medal record, @Param("example") MedalExample example);

    int updateByPrimaryKeySelective(Medal record);

    int updateByPrimaryKey(Medal record);
}