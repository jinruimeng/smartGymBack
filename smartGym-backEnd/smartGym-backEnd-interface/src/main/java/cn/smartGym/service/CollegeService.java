package cn.smartGym.service;

/**
 * 学院管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface CollegeService {
	String getCollege(Integer id);

	Integer getId(String college);
}
