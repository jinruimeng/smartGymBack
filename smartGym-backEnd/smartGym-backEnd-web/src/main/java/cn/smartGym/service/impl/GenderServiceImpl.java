package cn.smartGym.service.impl;

import org.springframework.stereotype.Service;

import cn.smartGym.service.GenderService;

@Service
public class GenderServiceImpl implements GenderService {
	
	/**
	 * 性别字符串到编号转换
	 * @see cn.smartGym.service.GenderService#genderStrToInt(java.lang.String)
	 */
	@Override
	public Integer genderStrToInt(String gender) {
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
	 * 性别编号到字符串转换
	 * @see cn.smartGym.service.GenderService#genderIntToStr(java.lang.Integer)
	 */
	@Override
	public String genderIntToStr(Integer gender) {
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
