package cn.smartGym.controller.pojoCtr;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.GenderGroupService;
import cn.smartGym.service.ItemService;
import cn.smartGym.service.JobService;
import common.enums.GenderGroup;
import common.enums.Job;

public class ApplicationCtr implements Serializable{

	private static final long serialVersionUID = 896372022414526179L;
	
	@Autowired
	private CollegeService collegeService;

	@Autowired
	private GenderGroupService genderGroupService;

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemService itemService;
	
	private Long id;
	
	private String name;
	
	private String college;

	private String job;

	private String gender;

	private String studentNo;

	private Long itemId;//不用接收

	private Integer status;//不用接收

	private String game;

	private String category;

	private String item;
	
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public ApplicationCtr() {
		super();
	}

	public ApplicationCtr(Application application) {
		ApplicationCtr applicationCtr = new ApplicationCtr();
		applicationCtr.setId(application.getId());
		applicationCtr.setName(application.getName());
		applicationCtr.setCollege(collegeService.getCollege(application.getCollege()));
		applicationCtr.setJob(jobService.jobIntToString(application.getJob()));
		applicationCtr.setGender(genderGroupService.genderIntToStr(application.getGender()));
		applicationCtr.setStudentNo(application.getStudentNo());
		applicationCtr.setItemId(application.getItemId());
		Item item = itemService.getItemByItemId(application.getItemId(), null);
		applicationCtr.setItem(item.getItem());
		applicationCtr.setGame(item.getGame());
		applicationCtr.setCategory(item.getCategory());
	}
	
	public Application toDao(ApplicationCtr applicationCtr) {
		Item item = new Item();
		item.setGame(applicationCtr.getGame());
		item.setCategory(applicationCtr.getCategory());
		item.setItem(applicationCtr.getItem());
		item.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
		// 根据具体项目信息查找itemId
		List<Item> items = itemService.getItemsByItemDetails(item);
		if (items == null || items.isEmpty())
			return null;
		
		Application application = new Application();
		application.setId(applicationCtr.getId());
		application.setName(applicationCtr.getName());
		application.setCollege(collegeService.getId(applicationCtr.getCollege()));
		application.setJob(Job.getIndex(applicationCtr.getJob()));
		application.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
		application.setStudentNo(applicationCtr.getStudentNo());
		application.setItemId(items.get(0).getId());
		application.setStatus(applicationCtr.getStatus());
		return application;
	}

}