package cn.smartGym.service;

/**
 * 校区管理服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface CampusService {
	String getCampus(Integer id);

	Integer getId(String campus);
}
