package cn.smartGym.pojoCtr;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ItemCtr implements Serializable {

	private static final long serialVersionUID = 6605952592376124670L;

	private Long id;

	private String game;

	private String category;

	private String item;

	private String gender;

	private Integer pathNum;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date date;

	private String place;

	private Integer participantNum;

	private Integer status;

	private String description;
	
	private Integer type;
	
	private Long typeId;
	
	private Integer rankCriterion;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Integer getRankCriterion() {
		return rankCriterion;
	}

	public void setRankCriterion(Integer rankCriterion) {
		this.rankCriterion = rankCriterion;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getParticipantNum() {
		return participantNum;
	}

	public void setParticipantNum(Integer participantNum) {
		this.participantNum = participantNum;
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

	public Integer getPathNum() {
		return pathNum;
	}

	public void setPathNum(Integer pathNum) {
		this.pathNum = pathNum;
	}

	public ItemCtr() {
		super();
	}

}
