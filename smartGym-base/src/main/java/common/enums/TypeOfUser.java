package common.enums;

public enum TypeOfUser {
	student(0, "学生"), teacher(1, "教师");

	private Integer type;
	private String typeName;

	TypeOfUser(Integer type, String typeName) {
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
		for (TypeOfUser i : TypeOfUser.values())
			if (i.getType() == type)
				return i.typeName;
		return null;
	}

	public static Integer getType(String typeName) {
		for (TypeOfUser i : TypeOfUser.values())
			if (i.getTypeName().equals(typeName))
				return i.type;
		return null;
	}
}
