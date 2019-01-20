package common.enums;

public enum ErrorCode {
	SUCCESS(200, "操作成功！"), ARGUMENT_EXCEPTION(300, "参数无效！"), SYSTEM_EXCEPTION(400, "系统内部错误！"),
	BUSINESS_EXCEPTION(500, "业务异常！");

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
