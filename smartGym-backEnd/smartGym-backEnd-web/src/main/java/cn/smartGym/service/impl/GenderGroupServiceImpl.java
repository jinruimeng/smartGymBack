package cn.smartGym.service.impl;


import org.springframework.stereotype.Service;

import cn.smartGym.service.GenderGroupService;

@Service
public class GenderGroupServiceImpl implements GenderGroupService {

	/**
	 * 性别字符串到编号转换
	 * @see cn.smartGym.service.GenderService#genderStrToInt(java.lang.String)
	 */
	@Override
	public Integer genderStrToInt(String gender) {
		switch (gender) {
		case "男子组":
			return 0;
		case "女子组":
			return 1;
		case "男女混合":
			return 2;
		default:
			return 2;
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
			return "男子组";
		case 1:
			return "女子组";
		case 2:
			return "男女混合";
		default:
			return "男女混合";
		}
	}

}
