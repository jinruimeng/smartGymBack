package cn.smartGym.utils;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;

@Component
public class CollegeAndCampusUtils {

	@Autowired
	private CollegeService collegeService;
	@Autowired
	private CampusService campusService;
	
	public static CollegeAndCampusUtils collegeAndCampusUtils;

	@PostConstruct
	public void init() {
		collegeAndCampusUtils = this;
	}
	
	public static String getCollegeName(Integer id) {
		return collegeAndCampusUtils.collegeService.getCollege(id);
	}

	public static Integer getCollegeIndex(String college) {
		return collegeAndCampusUtils.collegeService.getId(college);
	}

	public static Map<Integer, String> getAllCollegeIdsAndName() {
		return collegeAndCampusUtils.collegeService.getAllCollegeIdsAndName();
	}

	public static String getCampusName(Integer id) {
		return collegeAndCampusUtils.campusService.getCampus(id);
	}

	public static Integer getCampusIndex(String college) {
		return collegeAndCampusUtils.campusService.getId(college);
	}

	public static List<String> getAllCampuses() {
		return collegeAndCampusUtils.campusService.getAllCampuses();
	}
}
