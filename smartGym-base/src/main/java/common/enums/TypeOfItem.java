package common.enums;

public enum TypeOfItem {
	university(0, "学校活动"), college(1, "学院活动"), teacher(2, "教师活动"), association(3, "协会活动");

	private Integer type;
	private String typeName;

	TypeOfItem(Integer type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}

	public Integer getType() {
		return this.type;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public static String getTypeName(Integer type) {
		for (TypeOfItem i : TypeOfItem.values())
			if (i.getType() == type)
				return i.typeName;
		return null;
	}

	public static Integer getType(String typeName) {
		for (TypeOfItem i : TypeOfItem.values())
			if (i.getTypeName().equals(typeName))
				return i.type;
		return null;
	}
}
