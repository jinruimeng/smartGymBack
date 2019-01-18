package common.enums;

public enum GenderGroup {
	male(0, "男子组"), female(1, "女子组"), mix(2, "男女混合");

	private Integer index;
	private String name;

	private GenderGroup(Integer index, String name) {
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
		for (GenderGroup i : GenderGroup.values())
			if (i.getIndex() == index)
				return i.name;
		return null;
	}

	public static Integer getIndex(String name) {
		for (GenderGroup i : GenderGroup.values())
			if (i.getName().equals(name))
				return i.index;
		return null;
	}
}
