package common.exceptions;

import common.enums.ErrorCode;

public class SystemException extends BaseException {
	private static final long serialVersionUID = -3251905855726035118L;

	public SystemException(String errorMessage) {
		super(ErrorCode.SYSTEM_EXCEPTION.getErrorCode(), errorMessage);
	}

	public SystemException() {
		super(ErrorCode.SYSTEM_EXCEPTION.getErrorCode(), ErrorCode.SYSTEM_EXCEPTION.getErrorMessage());
	}
}
