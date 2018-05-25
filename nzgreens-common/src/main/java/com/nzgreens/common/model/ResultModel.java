package com.nzgreens.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author fei
 * @version V1.0
 */
public class ResultModel<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JSONField(ordinal = 0)
	private boolean success = true;

	@JSONField(ordinal = 1)
	private String errorCode;

	@JSONField(ordinal = 2)
	private String errorInfo;

	@JSONField(ordinal = 3)
	private T data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String toJson() {
		return JSON.toJSONString(this);
	}

}
