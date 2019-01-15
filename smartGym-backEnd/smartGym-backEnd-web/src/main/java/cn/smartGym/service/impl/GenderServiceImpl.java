package cn.smartGym.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.smartGym.service.GenderService;

/**
 * 性别管理Service
 * 
 * @author Ruimeng Jin
 *
 */
@Service
public class GenderServiceImpl implements GenderService {

	/**
	 * 性别 字符串到编号转换
	 * 
	 * @see cn.smartGym.service.GenderService#genderStrToInt(java.lang.String)
	 */
	@Override
	public Integer genderStrToInt(String gender) {
		if (StringUtils.isBlank(gender))
			return null;

		switch (gender) {
		case "男":
			return 0;
		case "女":
			return 1;
		default:
			return 0;
		}
	}

	/**
	 * 性别 编号到字符串转换
	 * 
	 * @see cn.smartGym.service.GenderService#genderIntToStr(java.lang.Integer)
	 */
	@Override
	public String genderIntToStr(Integer gender) {
		if (gender == null)
			return null;

		switch (gender) {
		case 0:
			return "男";
		case 1:
			return "女";
		default:
			return "男";
		}
	}

}
