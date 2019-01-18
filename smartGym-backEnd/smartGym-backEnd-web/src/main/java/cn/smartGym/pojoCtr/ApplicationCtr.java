package cn.smartGym.pojoCtr;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ApplicationCtr implements Serializable {

	private static final long serialVersionUID = 896372022414526179L;

//	@Autowired
//	private CollegeService collegeService;
//
//	@Autowired
//	private ItemService itemService;

	private Long id;

	private String name;

	private String college;

	private String job;

	private String gender;

	private String studentNo;

	private Long itemId;

	private Integer status;

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

//	public ApplicationCtr(Application application) {
//		super();
//		this = ConversionUtil.applicationDaoToCtr(application);
//		this.id = application.getId();
//		this.name = application.getName();
//		this.college = collegeService.getCollege(application.getCollege());
//		this.job = Job.getName(application.getJob());
//		this.gender = GenderGroup.getName(application.getGender());
//		this.studentNo = application.getStudentNo();
//		this.itemId = application.getItemId();
//		Item item = itemService.getItemByItemIdAndStatuses(application.getItemId(), new Integer[0]);
//		if (item == null)
//			return;
//		this.item = item.getItem();
//		this.game = item.getGame();
//		this.category = item.getCategory();
//	}

//	public Application toDao() {
//		Application application = new Application();
//
//		Item item = new Item();
//		item.setGame(this.getGame());
//		item.setCategory(this.getCategory());
//		item.setItem(this.getItem());
//		item.setGender(GenderGroup.getIndex(this.getGender()));
//		// 根据具体项目信息查找itemId
//		List<Item> items = itemService.getItemsByDetailsAndStatuses(item, new Integer[0]);
//		List<Long> itemIds = itemService.getItemIdsByItems(items);
//		if (itemIds == null || itemIds.isEmpty())
//			return application;
//
//		application.setId(this.getId());
//		application.setName(this.getName());
//		application.setCollege(collegeService.getId(this.getCollege()));
//		application.setJob(Job.getIndex(this.getJob()));
//		application.setGender(GenderGroup.getIndex(this.getGender()));
//		application.setStudentNo(this.getStudentNo());
//		application.setItemId(itemIds.get(0));
//		application.setStatus(this.getStatus());
//		return application;
//	}

//	public static List<ApplicationCtr> daoListToCtrList(List<Application> applications) {
//		ArrayList<ApplicationCtr> applicationsCtr = new ArrayList<>();
//		for (Application application : applications) {
//			applicationsCtr.add(new ApplicationCtr(application));
//		}
//		return applicationsCtr;
//	}
}