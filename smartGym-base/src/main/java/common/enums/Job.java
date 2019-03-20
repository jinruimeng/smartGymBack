package common.enums;

public enum Job {
	player(0, "队员"), captain(1, "领队"), coach(2, "教练"), contactPerson(3, "联系人员"), volunteer(4, "志愿者"),
	manager(5, "管理人员");

	private Integer index;
	private String name;

	private Job(Integer index, String name) {
		this.index = index;
		this.name = name;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getName(Integer index) {
		for (Job i : Job.values())
			if (i.getIndex() == index)
				return i.name;
		return null;
	}

	public static Integer getIndex(String name) {
		for (Job i : Job.values())
			if (i.getName().equals(name))
				return i.index;
		return null;
	}
}
