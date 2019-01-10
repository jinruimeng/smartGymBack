package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymInform;
import cn.smartGym.pojo.SmartgymInformExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymInformMapper {
    long countByExample(SmartgymInformExample example);

    int deleteByExample(SmartgymInformExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymInform record);

    int insertSelective(SmartgymInform record);

    List<SmartgymInform> selectByExample(SmartgymInformExample example);

    SmartgymInform selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymInform record, @Param("example") SmartgymInformExample example);

    int updateByExample(@Param("record") SmartgymInform record, @Param("example") SmartgymInformExample example);

    int updateByPrimaryKeySelective(SmartgymInform record);

    int updateByPrimaryKey(SmartgymInform record);
}