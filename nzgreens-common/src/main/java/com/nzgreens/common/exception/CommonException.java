package com.nzgreens.common.exception;

public class CommonException extends Exception {

	private String errorCode;

	public CommonException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public CommonException(String errorCode) {
		this.errorCode = errorCode;
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public CommonException(String errorCode, String errorMsg, Throwable cause) {
		super(errorMsg, cause);
		this.errorCode = errorCode;
	}

	public CommonException() {
		super();
	}

	public String getErrorCode() {
		return errorCode;
	}
}
