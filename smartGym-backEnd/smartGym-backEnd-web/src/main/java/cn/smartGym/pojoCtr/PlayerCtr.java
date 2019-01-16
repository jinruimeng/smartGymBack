package cn.smartGym.pojoCtr;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PlayerCtr implements Serializable {

	private static final long serialVersionUID = -1254682028835756571L;

//	@Autowired
//	private CollegeService collegeService;
//
//	@Autowired
//	private ItemService itemService;

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

//	public PlayerCtr(Player player) {
//		super();
//
//		Item item = itemService.getItemByItemIdAndStatuses(player.getItemId(), new Integer[0]);
//		if (item == null)
//			return;
//		this.game = item.getGame();
//		this.category = item.getCategory();
//		this.item = item.getItem();
//		this.itemId = player.getItemId();
//		this.id = player.getId();
//		this.name = player.getName();
//		this.college = collegeService.getCollege(player.getCollege());
//		this.studentNo = player.getStudentNo();
//		this.job = Job.getName(player.getJob());
//		this.playerNo = player.getPlayerNo();
//		this.gender = GenderGroup.getName(player.getGender());
//		this.groupNo = player.getGroupNo();
//		this.pathNo = player.getPathNo();
//		this.grades = player.getGrades();
//		this.rankNo = player.getRankNo();
//		this.status = player.getStatus();
//	}
//
//	public Player toDao(PlayerCtr playerCtr) {
//		Player player = new Player();
//
//		Item item = new Item();
//		item.setGame(playerCtr.getGame());
//		item.setCategory(playerCtr.getCategory());
//		item.setItem(playerCtr.getItem());
//		item.setGender(GenderGroup.getIndex(playerCtr.getGender()));
//		List<Item> items = itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
//		List<Long> itemIds = itemService.getItemIdsByItems(items);
//		if (itemIds == null || itemIds.size() == 0)
//			return player;
//		playerCtr.setItemId(itemIds.get(0));
//
//		player.setId(playerCtr.getId());
//		player.setName(playerCtr.getName());
//		player.setCollege(collegeService.getId(playerCtr.getCollege()));
//		player.setStudentNo(playerCtr.getStudentNo());
//		player.setItemId(playerCtr.getItemId());
//		player.setJob(Job.getIndex(playerCtr.getJob()));
//		player.setGender(GenderGroup.getIndex(playerCtr.getGender()));
//		player.setPlayerNo(playerCtr.getPlayerNo());
//		player.setGroupNo(playerCtr.getGroupNo());
//		player.setPathNo(playerCtr.getPathNo());
//		player.setGrades(playerCtr.getGrades());
//		player.setRankNo(playerCtr.getRankNo());
//		player.setStatus(playerCtr.getStatus());
//
//		return player;
//	}
//
//	public static List<PlayerCtr> daoListToCtrList(List<Player> players) {
//		ArrayList<PlayerCtr> playersCtr = new ArrayList<>();
//		for (Player player : players) {
//			playersCtr.add(new PlayerCtr(player));
//		}
//		return playersCtr;
//	}
}