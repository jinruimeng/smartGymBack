package common.exceptions;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 3246823305449180512L;

	private Integer errorCode;
	private String errorMessage;

	public BaseException(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
