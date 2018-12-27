package cn.smartGym.service;

import java.util.List;

/**
 * 学院管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface CollegeService {
	String getCollege(Integer id);

	Integer getId(String college);
	
	List<String> getAllcolleges();
}
