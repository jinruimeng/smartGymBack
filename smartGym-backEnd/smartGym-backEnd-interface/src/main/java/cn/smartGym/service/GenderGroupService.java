package cn.smartGym.service;

/**
 * 项目性别要求管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface GenderGroupService {
	Integer genderStrToInt(String gender);

	String genderIntToStr(Integer gender);
}
