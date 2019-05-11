package cn.smartGym.pojoCtr;

import java.io.Serializable;

public class PlayerCtr implements Serializable {

	private static final long serialVersionUID = -1254682028835756571L;

	private Long id;

	private String name;

	private String college;

	private String studentNo;

	private Long itemId;

	private String game;

	private String category;

	private String item;

	private String job;

	private String playerNo;

	private String gender;

	private Integer groupNo;

	private Integer pathNo;

	private String grades;

	private String finalGrades;

	private Integer rankNo;

	private Integer status;

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public Integer getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	public Integer getPathNo() {
		return pathNo;
	}

	public void setPathNo(Integer pathNo) {
		this.pathNo = pathNo;
	}

	public String getGrades() {
		return grades;
	}

	public void setGrades(String grades) {
		this.grades = grades;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getPlayerNo() {
		return playerNo;
	}

	public void setPlayerNo(String playerNo) {
		this.playerNo = playerNo;
	}

	public Integer getRankNo() {
		return rankNo;
	}

	public void setRankNo(Integer rankNo) {
		this.rankNo = rankNo;
	}

	public PlayerCtr() {
		super();
		// TODO 自动生成的构造函数存根
	}

	public String getFinalGrades() {
		return finalGrades;
	}

	public void setFinalGrades(String finalGrades) {
		this.finalGrades = finalGrades;
	}

}