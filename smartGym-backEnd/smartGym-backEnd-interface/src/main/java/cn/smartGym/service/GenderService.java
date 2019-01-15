package cn.smartGym.service;

/**
 * 性别管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface GenderService {
	Integer genderStrToInt(String gender);

	String genderIntToStr(Integer gender);
}
