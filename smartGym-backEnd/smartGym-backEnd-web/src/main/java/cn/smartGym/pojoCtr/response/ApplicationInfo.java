package cn.smartGym.pojoCtr.response;

import java.io.Serializable;

public class ApplicationInfo implements Serializable{

	private static final long serialVersionUID = 1863863478747099499L;
	
	private Long itemId;
	
	private String college;
	
	private String game;

	private String category;

	private String item;

	private String gender;
	
	private Integer needNumber;
	
	private Long applicationNumber;
	
	private Long reviewNumber;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getNeedNumber() {
		return needNumber;
	}

	public void setNeedNumber(Integer needNumber) {
		this.needNumber = needNumber;
	}

	public Long getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(Long applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public Long getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(Long reviewNumber) {
		this.reviewNumber = reviewNumber;
	}
	
}
