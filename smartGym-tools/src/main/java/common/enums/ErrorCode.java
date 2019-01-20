package common.enums;

public enum ErrorCode {
	SUCCESS(200, "操作成功！"), NO_CONTENT(201, "数据库无内容！"), BAD_REQUEST(400, "参数无效！"), UNAUTHORIZED(401, "权限不足！"),
	CONFLICT(409, "与数据库中记录冲突！"), SYSTEM_EXCEPTION(500, "服务器内部错误！"), BUSINESS_EXCEPTION(501, "业务异常！");
	
	private Integer errorCode;
	private String errorMessage;

	ErrorCode(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
