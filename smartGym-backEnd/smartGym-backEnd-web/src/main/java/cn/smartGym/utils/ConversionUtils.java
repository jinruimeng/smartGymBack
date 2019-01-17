package cn.smartGym.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoCtr.ApplicationCtr;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.pojoCtr.SgUserCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ItemService;
import common.enums.Gender;
import common.enums.GenderGroup;
import common.enums.Job;

@Component
public class ConversionUtils {

	@Autowired
	private CollegeService collegeService;
	@Autowired
	private CampusService campusService;
	@Autowired
	private ItemService itemService;

	public static ConversionUtils conversionUtil;

	@PostConstruct
	public void init() {
		conversionUtil = this;
	}

	public static Application applicationCtrToDao(ApplicationCtr applicationCtr) {
		Application application = new Application();

		Item item = new Item();
		item.setGame(applicationCtr.getGame());
		item.setCategory(applicationCtr.getCategory());
		item.setItem(applicationCtr.getItem());
		item.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
		// 根据具体项目信息查找itemId
		List<Item> items = conversionUtil.itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
		List<Long> itemIds = conversionUtil.itemService.getItemIdsByItems(items);
		if (itemIds == null || itemIds.isEmpty())
			return application;

		application.setId(applicationCtr.getId());
		application.setName(applicationCtr.getName());
		application.setCollege(conversionUtil.collegeService.getId(applicationCtr.getCollege()));
		application.setJob(Job.getIndex(applicationCtr.getJob()));
		application.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
		application.setStudentNo(applicationCtr.getStudentNo());
		application.setItemId(itemIds.get(0));
		application.setStatus(applicationCtr.getStatus());
		return application;
	}

	public static ApplicationCtr applicationDaoToCtr(Application application) {
		ApplicationCtr applicationCtr = new ApplicationCtr();

		Item item = conversionUtil.itemService.getItemByItemIdAndStatuses(application.getItemId(), new Integer[0]);
		if (item == null)
			return applicationCtr;

		applicationCtr.setId(application.getId());
		applicationCtr.setName(application.getName());
		applicationCtr.setCollege(conversionUtil.collegeService.getCollege(application.getCollege()));
		applicationCtr.setJob(Job.getName(application.getJob()));
		applicationCtr.setGender(GenderGroup.getName(application.getGender()));
		applicationCtr.setStudentNo(application.getStudentNo());
		applicationCtr.setItemId(application.getItemId());
		applicationCtr.setStatus(application.getStatus());
		applicationCtr.setItem(item.getItem());
		applicationCtr.setGame(item.getGame());
		applicationCtr.setCategory(item.getCategory());
		return applicationCtr;
	}

	public static List<ApplicationCtr> applicationdaoListToCtrList(List<Application> applications) {
		ArrayList<ApplicationCtr> applicationsCtr = new ArrayList<>();
		for (Application application : applications) {
			applicationsCtr.add(applicationDaoToCtr(application));
		}
		return applicationsCtr;
	}

	public static ItemCtr itemDaoToCtr(Item item) {
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

		return itemCtr;
	}

	public static Item itemCtrtoDao(ItemCtr itemCtr) {
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

	public static List<ItemCtr> itemDaoListToCtrList(List<Item> items) {
		ArrayList<ItemCtr> itemsCtr = new ArrayList<>();
		for (Item item : items) {
			itemsCtr.add(itemDaoToCtr(item));
		}
		return itemsCtr;
	}

	public static PlayerCtr playerDaoToCtr(Player player) {
		PlayerCtr playCtr = new PlayerCtr();

		Item item = conversionUtil.itemService.getItemByItemIdAndStatuses(player.getItemId(), new Integer[0]);
		if (item == null)
			return playCtr;

		playCtr.setGame(item.getGame());
		playCtr.setCategory(item.getCategory());
		playCtr.setItem(item.getItem());
		playCtr.setItemId(player.getItemId());
		playCtr.setId(player.getId());
		playCtr.setName(player.getName());
		playCtr.setCollege(conversionUtil.collegeService.getCollege(player.getCollege()));
		playCtr.setStudentNo(player.getStudentNo());
		playCtr.setJob(Job.getName(player.getJob()));
		playCtr.setPlayerNo(player.getPlayerNo());
		playCtr.setGender(GenderGroup.getName(player.getGender()));
		playCtr.setGroupNo(player.getGroupNo());
		playCtr.setPathNo(player.getPathNo());
		playCtr.setGrades(player.getGrades());
		playCtr.setRankNo(player.getRankNo());
		playCtr.setStatus(player.getStatus());

		return playCtr;
	}

	public static Player playCtrToDao(PlayerCtr playerCtr) {
		Player player = new Player();

		Item item = new Item();
		item.setGame(playerCtr.getGame());
		item.setCategory(playerCtr.getCategory());
		item.setItem(playerCtr.getItem());
		item.setGender(GenderGroup.getIndex(playerCtr.getGender()));
		List<Item> items = conversionUtil.itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
		List<Long> itemIds = conversionUtil.itemService.getItemIdsByItems(items);
		if (itemIds == null || itemIds.size() == 0)
			return player;
		playerCtr.setItemId(itemIds.get(0));

		player.setId(playerCtr.getId());
		player.setName(playerCtr.getName());
		player.setCollege(conversionUtil.collegeService.getId(playerCtr.getCollege()));
		player.setStudentNo(playerCtr.getStudentNo());
		player.setItemId(playerCtr.getItemId());
		player.setJob(Job.getIndex(playerCtr.getJob()));
		player.setGender(GenderGroup.getIndex(playerCtr.getGender()));
		player.setPlayerNo(playerCtr.getPlayerNo());
		player.setGroupNo(playerCtr.getGroupNo());
		player.setPathNo(playerCtr.getPathNo());
		player.setGrades(playerCtr.getGrades());
		player.setRankNo(playerCtr.getRankNo());
		player.setStatus(playerCtr.getStatus());

		return player;
	}

	public static List<PlayerCtr> playerDaoListToCtrList(List<Player> players) {
		ArrayList<PlayerCtr> playersCtr = new ArrayList<>();
		for (Player player : players) {
			playersCtr.add(playerDaoToCtr(player));
		}
		return playersCtr;
	}

	public static SgUserCtr userDaoToCtr(SgUser user) {
		SgUserCtr userCtr = new SgUserCtr();
		userCtr.setId(user.getId());
		userCtr.setName(user.getName());
		userCtr.setWxId(user.getWxId());
		userCtr.setStudentNo(user.getStudentNo());
		userCtr.setGender(Gender.getName(user.getGender()));
		userCtr.setCampus(conversionUtil.campusService.getCampus(user.getCampus()));
		userCtr.setCollege(conversionUtil.collegeService.getCollege(user.getCollege()));
		userCtr.setPhone(user.getPhone());
		userCtr.setStatus(user.getStatus());
		userCtr.setAuthority(user.getAuthority());
		return userCtr;
	}

	public static SgUser userCtrToDao(SgUserCtr userCtr) {
		SgUser user = new SgUser();

		user.setId(userCtr.getId());
		user.setName(userCtr.getName());
		user.setWxId(userCtr.getWxId());
		user.setStudentNo(userCtr.getStudentNo());
		user.setGender(Gender.getIndex(userCtr.getGender()));
		user.setCampus(conversionUtil.campusService.getId(userCtr.getCampus()));
		user.setCollege(conversionUtil.collegeService.getId(userCtr.getCollege()));
		user.setPhone(userCtr.getPhone());
		user.setStatus(userCtr.getStatus());
		user.setAuthority(userCtr.getAuthority());

		return user;
	}

	public static List<SgUserCtr> daoListToCtrList(List<SgUser> users) {
		List<SgUserCtr> usersCtr = new ArrayList<>();
		for (SgUser user : users) {
			usersCtr.add(userDaoToCtr(user));
		}
		return usersCtr;
	}

	public static String getCollegeName(Integer id) {
		return conversionUtil.collegeService.getCollege(id);
	}

	public static Integer getCollegeIndex(String college) {
		return conversionUtil.collegeService.getId(college);
	}

	public static Map<Integer, String> getAllCollegeIdsAndName() {
		return conversionUtil.collegeService.getAllCollegeIdsAndName();
	}

	public static String getCampusName(Integer id) {
		return conversionUtil.campusService.getCampus(id);
	}

	public static Integer getCampusIndex(String college) {
		return conversionUtil.campusService.getId(college);
	}

	public static List<String> getAllCampuses() {
		return conversionUtil.campusService.getAllCampuses();
	}
}
