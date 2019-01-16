package cn.smartGym.pojoCtr;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
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

//	public ItemCtr(Item item) {
//		super();
//		this.id = item.getId();
//		this.game = item.getGame();
//		this.category = item.getCategory();
//		this.item = item.getItem();
//		this.gender = GenderGroup.getName(item.getGender());
//		this.pathNum = item.getPathNum();
//		this.date = item.getDate();
//		this.place = item.getPlace();
//		this.participantNum = item.getParticipantNum();
//		this.status = item.getStatus();
//		this.description = item.getDescription();
//	}
//
//	public Item toDao() {
//		Item item = new Item();
//		item.setId(this.getId());
//		item.setGame(this.getGame());
//		item.setCategory(this.getCategory());
//		item.setItem(this.getItem());
//		item.setGender(GenderGroup.getIndex(this.getGender()));
//		item.setPathNum(this.getPathNum());
//		item.setDate(this.getDate());
//		item.setPlace(this.getPlace());
//		item.setParticipantNum(this.getParticipantNum());
//		item.setDescription(this.getDescription());
//		item.setStatus(this.getStatus());
//		return item;
//	}
//
//	public static List<ItemCtr> daoListToCtrList(List<Item> items) {
//		ArrayList<ItemCtr> itemsCtr = new ArrayList<>();
//		for (Item item : items) {
//			itemsCtr.add(new ItemCtr(item));
//		}
//		return itemsCtr;
//	}
}
