package cn.smartGym.mapper;

import cn.smartGym.pojo.SmartgymItems;
import cn.smartGym.pojo.SmartgymItemsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmartgymItemsMapper {
    long countByExample(SmartgymItemsExample example);

    int deleteByExample(SmartgymItemsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmartgymItems record);

    int insertSelective(SmartgymItems record);

    List<SmartgymItems> selectByExample(SmartgymItemsExample example);

    SmartgymItems selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmartgymItems record, @Param("example") SmartgymItemsExample example);

    int updateByExample(@Param("record") SmartgymItems record, @Param("example") SmartgymItemsExample example);

    int updateByPrimaryKeySelective(SmartgymItems record);

    int updateByPrimaryKey(SmartgymItems record);
}