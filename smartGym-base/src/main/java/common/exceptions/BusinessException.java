package common.exceptions;

import common.enums.ErrorCode;

public class BusinessException extends BaseException {
	private static final long serialVersionUID = 655043713402369119L;

	public BusinessException(String errorMessage) {
		super(ErrorCode.BUSINESS_EXCEPTION.getErrorCode(), errorMessage);
	}

	public BusinessException() {
		super(ErrorCode.BUSINESS_EXCEPTION.getErrorCode(), ErrorCode.BUSINESS_EXCEPTION.getErrorMessage());
	}
}
