package com.bce.model;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
public class CustomResponse<T> {

	T data;

	private String message;
	private String status;

	private Object token;
	
	public CustomResponse(T obj) {
		super();
		this.data = obj;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

}
