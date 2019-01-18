package cn.smartGym.service;

import java.util.List;

/**
 * 校区管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface CampusService {
	String getCampus(Integer id);

	Integer getId(String campus);

	List<String> getAllCampuses();
}
