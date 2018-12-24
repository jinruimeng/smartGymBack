package cn.smartGym.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojo.SmartgymUsersExample;

public interface SmartgymUsersMapper {
    long countByExample(SmartgymUsersExample example);

    int deleteByExample(SmartgymUsersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymUsers record);

    int insertSelective(SmartgymUsers record);

    List<SmartgymUsers> selectByExample(SmartgymUsersExample example);

    SmartgymUsers selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymUsers record, @Param("example") SmartgymUsersExample example);

    int updateByExample(@Param("record") SmartgymUsers record, @Param("example") SmartgymUsersExample example);

    int updateByPrimaryKeySelective(SmartgymUsers record);

    int updateByPrimaryKey(SmartgymUsers record);
}