package cn.smartGym.controller.pojoCtr;

import java.io.Serializable;
import java.util.Date;

import cn.smartGym.pojo.Item;
import common.enums.GenderGroup;

public class ItemCtr implements Serializable {

	private static final long serialVersionUID = 6605952592376124670L;

	private Long id;

	private String game;

	private String category;

	private String item;

	private String gender;

	private Integer pathNum;
	
	private Date date;

	private String place;

	private Integer participantNum;

	private Integer status;
	
	private String description;

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
	
	public ItemCtr(Item item) {
		ItemCtr itemCtr = new ItemCtr();
		itemCtr.setId(item.getId());
		itemCtr.setGame(item.getGame());
		itemCtr.setCategory(item.getCategory());
		itemCtr.setItem(item.getItem());
		itemCtr.setGender(GenderGroup.getName(item.getGender()));
		itemCtr.setPathNum(item.getPathNum());
		itemCtr.setDate(item.getDate());
		itemCtr.setPlace(item.getPlace());
		itemCtr.setParticipantNum(item.getParticipantNum());
		itemCtr.setStatus(item.getStatus());
		itemCtr.setDescription(item.getDescription());

	}
	
	public Item toDao(ItemCtr itemCtr) {
		Item item = new Item();
		item.setId(itemCtr.getId());
		item.setGame(itemCtr.getGame());
		item.setCategory(itemCtr.getCategory());
		item.setItem(itemCtr.getItem());
		item.setGender(GenderGroup.getIndex(itemCtr.getGender()));
		item.setPathNum(itemCtr.getPathNum());
		item.setDate(itemCtr.getDate());
		item.setPlace(itemCtr.getPlace());
		item.setParticipantNum(itemCtr.getParticipantNum());
		item.setDescription(itemCtr.getDescription());
		item.setStatus(itemCtr.getStatus());
		return item;
	}

}
