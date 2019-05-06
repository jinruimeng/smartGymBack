package cn.smartGym.mapper;

import cn.smartGym.pojo.Picture;
import cn.smartGym.pojo.PictureExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PictureMapper {
    long countByExample(PictureExample example);

    int deleteByExample(PictureExample example);

    int insert(Picture record);

    int insertSelective(Picture record);

    List<Picture> selectByExample(PictureExample example);

    int updateByExampleSelective(@Param("record") Picture record, @Param("example") PictureExample example);

    int updateByExample(@Param("record") Picture record, @Param("example") PictureExample example);
}