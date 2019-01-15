package cn.smartGym.mapper;

import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojo.SgUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SgUserMapper {
    long countByExample(SgUserExample example);

    int deleteByExample(SgUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SgUser record);

    int insertSelective(SgUser record);

    List<SgUser> selectByExample(SgUserExample example);

    SgUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SgUser record, @Param("example") SgUserExample example);

    int updateByExample(@Param("record") SgUser record, @Param("example") SgUserExample example);

    int updateByPrimaryKeySelective(SgUser record);

    int updateByPrimaryKey(SgUser record);
}