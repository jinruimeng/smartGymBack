package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojo.SmartgymApplicationsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymApplicationsMapper {
    long countByExample(SmartgymApplicationsExample example);

    int deleteByExample(SmartgymApplicationsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymApplications record);

    int insertSelective(SmartgymApplications record);

    List<SmartgymApplications> selectByExample(SmartgymApplicationsExample example);

    SmartgymApplications selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymApplications record, @Param("example") SmartgymApplicationsExample example);

    int updateByExample(@Param("record") SmartgymApplications record, @Param("example") SmartgymApplicationsExample example);

    int updateByPrimaryKeySelective(SmartgymApplications record);

    int updateByPrimaryKey(SmartgymApplications record);
}