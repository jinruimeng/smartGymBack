package common.enums;

public enum Gender {
	male(0, "男"), female(1, "女");

	private Integer index;
	private String name;

	private Gender(Integer index, String name) {
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
		for (Gender i : Gender.values())
			if (i.getIndex() == index)
				return i.name;
		return null;
	}

	public static Integer getIndex(String name) {
		for (Gender i : Gender.values())
			if (i.getName().equals(name))
				return i.index;
		return null;
	}
}
