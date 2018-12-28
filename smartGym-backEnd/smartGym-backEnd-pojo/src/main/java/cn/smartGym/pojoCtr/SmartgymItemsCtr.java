package cn.smartGym.pojoCtr;

import java.util.Date;

public class SmartgymItemsCtr {
	private String game;

	private String category;

	private String item;

	private String gender;
	
	private Date date;

    private String place;

    private Integer participantnums;
    
    private Integer applynums;
    
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

	public Integer getParticipantnums() {
		return participantnums;
	}

	public void setParticipantnums(Integer participantnums) {
		this.participantnums = participantnums;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getApplynums() {
		return applynums;
	}

	public void setApplynums(Integer applynums) {
		this.applynums = applynums;
	}

}
