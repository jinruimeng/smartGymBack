package cn.smartGym.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.smartGym.pojo.Application;
import cn.smartGym.pojo.Item;
import cn.smartGym.pojo.Medal;
import cn.smartGym.pojo.Player;
import cn.smartGym.pojo.SgUser;
import cn.smartGym.pojoCtr.ApplicationCtr;
import cn.smartGym.pojoCtr.ItemCtr;
import cn.smartGym.pojoCtr.MedalCtr;
import cn.smartGym.pojoCtr.PlayerCtr;
import cn.smartGym.pojoCtr.SgUserCtr;
import cn.smartGym.service.CampusService;
import cn.smartGym.service.CollegeService;
import cn.smartGym.service.ItemService;
import common.enums.Gender;
import common.enums.GenderGroup;
import common.enums.Job;
import common.enums.TypeOfItem;
import common.enums.TypeOfUser;

@Component
public class ConversionUtils {

	@Autowired
	private CollegeService collegeService;
	@Autowired
	private CampusService campusService;
	@Autowired
	private ItemService itemService;

	public static ConversionUtils conversionUtils;

	@PostConstruct
	public void init() {
		conversionUtils = this;
	}

	public static Application applicationCtrToDao(ApplicationCtr applicationCtr) {
		Application application = new Application();

		application.setId(applicationCtr.getId());
		application.setName(applicationCtr.getName());
		application.setCollege(conversionUtils.collegeService.getId(applicationCtr.getCollege()));
		application.setJob(Job.getIndex(applicationCtr.getJob()));
		application.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
		application.setStudentNo(applicationCtr.getStudentNo());
		application.setStatus(applicationCtr.getStatus());

		// 如果没有项目Id,去数据库中查
		if (applicationCtr.getItemId() == null) {
			Item item = new Item();
			item.setGame(applicationCtr.getGame());
			item.setCategory(applicationCtr.getCategory());
			item.setItem(applicationCtr.getItem());
			item.setGender(GenderGroup.getIndex(applicationCtr.getGender()));
			List<Item> items = conversionUtils.itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
			List<Long> itemIds = conversionUtils.itemService.getItemIdsByItems(items);
			if ((StringUtils.isBlank(applicationCtr.getGame()) && StringUtils.isBlank(applicationCtr.getCategory())
					&& StringUtils.isBlank(applicationCtr.getGender())) || itemIds == null || itemIds.size() == 0)
				return application;
			applicationCtr.setItemId(itemIds.get(0));
		}
		application.setItemId(applicationCtr.getItemId());

		return application;
	}

	public static ApplicationCtr applicationDaoToCtr(Application application) {
		ApplicationCtr applicationCtr = new ApplicationCtr();

		Item item = conversionUtils.itemService.getItemByItemIdAndStatuses(application.getItemId(), new Integer[0]);
		if (item == null)
			return applicationCtr;

		applicationCtr.setId(application.getId());
		applicationCtr.setName(application.getName());
		applicationCtr.setCollege(conversionUtils.collegeService.getCollege(application.getCollege()));
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
		itemCtr.setType(TypeOfItem.getTypeName(item.getType()));
		itemCtr.setTypeId(item.getTypeId());
		itemCtr.setRankCriterion(item.getRankCriterion());

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
		item.setType(TypeOfItem.getType(itemCtr.getType()));
		item.setTypeId(itemCtr.getTypeId());
		item.setRankCriterion(itemCtr.getRankCriterion());

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

		Item item = conversionUtils.itemService.getItemByItemIdAndStatuses(player.getItemId(), new Integer[0]);
		if (item == null)
			return playCtr;

		playCtr.setGame(item.getGame());
		playCtr.setCategory(item.getCategory());
		playCtr.setItem(item.getItem());
		playCtr.setItemId(player.getItemId());
		playCtr.setId(player.getId());
		playCtr.setName(player.getName());
		playCtr.setCollege(conversionUtils.collegeService.getCollege(player.getCollege()));
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

		player.setId(playerCtr.getId());
		player.setName(playerCtr.getName());
		player.setCollege(conversionUtils.collegeService.getId(playerCtr.getCollege()));
		player.setStudentNo(playerCtr.getStudentNo());
		player.setJob(Job.getIndex(playerCtr.getJob()));
		player.setGender(GenderGroup.getIndex(playerCtr.getGender()));
		player.setPlayerNo(playerCtr.getPlayerNo());
		player.setGroupNo(playerCtr.getGroupNo());
		player.setPathNo(playerCtr.getPathNo());
		player.setGrades(playerCtr.getGrades());
		player.setRankNo(playerCtr.getRankNo());
		player.setStatus(playerCtr.getStatus());

		// 如果没有项目Id,去数据库中查
		if (playerCtr.getItemId() == null) {
			Item item = new Item();
			item.setGame(playerCtr.getGame());
			item.setCategory(playerCtr.getCategory());
			item.setItem(playerCtr.getItem());
			item.setGender(GenderGroup.getIndex(playerCtr.getGender()));
			List<Item> items = conversionUtils.itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
			List<Long> itemIds = conversionUtils.itemService.getItemIdsByItems(items);
			if ((StringUtils.isBlank(playerCtr.getGame()) && StringUtils.isBlank(playerCtr.getCategory())
					&& StringUtils.isBlank(playerCtr.getGender())) || itemIds == null || itemIds.size() == 0)
				return player;
			playerCtr.setItemId(itemIds.get(0));
		}
		player.setItemId(playerCtr.getItemId());

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
		userCtr.setCampus(conversionUtils.campusService.getCampus(user.getCampus()));
		userCtr.setCollege(conversionUtils.collegeService.getCollege(user.getCollege()));
		userCtr.setPhone(user.getPhone());
		userCtr.setStatus(user.getStatus());
		userCtr.setAuthority(user.getAuthority());
		userCtr.setType(TypeOfUser.getTypeName(user.getType()));
		userCtr.setEmail(user.getEmail());
		return userCtr;
	}

	public static SgUser userCtrToDao(SgUserCtr userCtr) {
		SgUser user = new SgUser();

		user.setId(userCtr.getId());
		user.setName(userCtr.getName());
		user.setWxId(userCtr.getWxId());
		user.setStudentNo(userCtr.getStudentNo());
		user.setGender(Gender.getIndex(userCtr.getGender()));
		user.setCampus(conversionUtils.campusService.getId(userCtr.getCampus()));
		user.setCollege(conversionUtils.collegeService.getId(userCtr.getCollege()));
		user.setPhone(userCtr.getPhone());
		user.setStatus(userCtr.getStatus());
		user.setAuthority(userCtr.getAuthority());
		user.setType(TypeOfUser.getType(userCtr.getType()));
		user.setEmail(userCtr.getEmail());
		return user;
	}

	public static List<SgUserCtr> daoListToCtrList(List<SgUser> users) {
		List<SgUserCtr> usersCtr = new ArrayList<>();
		for (SgUser user : users) {
			usersCtr.add(userDaoToCtr(user));
		}
		return usersCtr;
	}

	public static MedalCtr medalDaoToCtr(Medal medal) {
		MedalCtr medalCtr = new MedalCtr();

		medalCtr.setId(medal.getId());
		medalCtr.setGame(medal.getGame());
		medalCtr.setCollege(conversionUtils.collegeService.getCollege(medal.getCollege()));
		medalCtr.setStatus(medal.getStatus());
		medalCtr.setFirst(medal.getFirst());
		medalCtr.setSecond(medal.getSecond());
		medalCtr.setThird(medal.getThird());
		medalCtr.setFourth(medal.getFourth());
		medalCtr.setFifth(medal.getFifth());
		medalCtr.setSixth(medal.getSixth());
		medalCtr.setSeventh(medal.getSeventh());
		medalCtr.setEighth(medal.getEighth());
		medalCtr.setScore(medal.getScore());
		medalCtr.setCreated(medal.getCreated());
		medalCtr.setUpdated(medal.getUpdated());
		return medalCtr;
	}

	public static Medal medalCtrToDao(MedalCtr medalCtr) {
		Medal medal = new Medal();

		medal.setId(medalCtr.getId());
		medal.setGame(medalCtr.getGame());
		medal.setCollege(conversionUtils.collegeService.getId(medalCtr.getCollege()));
		medal.setStatus(medalCtr.getStatus());
		medal.setFirst(medalCtr.getFirst());
		medal.setSecond(medalCtr.getSecond());
		medal.setThird(medalCtr.getThird());
		medal.setFourth(medalCtr.getFourth());
		medal.setFifth(medalCtr.getFifth());
		medal.setSixth(medalCtr.getSixth());
		medal.setSeventh(medalCtr.getSeventh());
		medal.setEighth(medalCtr.getEighth());
		medal.setScore(medalCtr.getScore());
		medal.setCreated(medalCtr.getCreated());
		medal.setUpdated(medalCtr.getUpdated());
		return medal;
	}

	public static List<MedalCtr> medalDaoListToCtrList(List<Medal> medals) {
		List<MedalCtr> medalCtrs = new ArrayList<>();
		for (Medal medal : medals) {
			medalCtrs.add(medalDaoToCtr(medal));
		}
		return medalCtrs;
	}

}
