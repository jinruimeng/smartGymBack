package common.exceptions;

import common.enums.ErrorCode;

public class ArgumentException extends BaseException{

	private static final long serialVersionUID = 6494764727454808573L;

	public ArgumentException(String errorMessage) {
        super(ErrorCode.ARGUMENT_EXCEPTION.getErrorCode(), errorMessage);
    }
	
	public ArgumentException() {
        super(ErrorCode.ARGUMENT_EXCEPTION.getErrorCode(), ErrorCode.ARGUMENT_EXCEPTION.getErrorMessage());
    }
}
