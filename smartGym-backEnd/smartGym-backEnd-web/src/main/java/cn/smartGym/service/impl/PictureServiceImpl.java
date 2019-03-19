package cn.smartGym.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.PictureMapper;
import cn.smartGym.pojo.Picture;
import cn.smartGym.pojo.PictureExample;
import cn.smartGym.pojo.PictureExample.Criteria;
import cn.smartGym.service.PictureService;
import common.enums.ErrorCode;
import common.utils.SGResult;
@Service
public class PictureServiceImpl implements PictureService {

	@Autowired
	private PictureMapper pictureMapper;
	
	@Override
	public SGResult getPictureUrl(Integer type) {
		PictureExample example = new PictureExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andStatusEqualTo(1);
		List<Picture> list = pictureMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "返回图片地址列表失败！");
		}
		return SGResult.build(200, "返回图片地址列表成功！", list);
	}

}
