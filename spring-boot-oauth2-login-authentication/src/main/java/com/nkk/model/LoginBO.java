package com.nkk.model;

import com.nkk.enume.DeviceType;
import com.nkk.enume.LoginTypeEnum;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
public class LoginBO {
	private String emailId;
	private String password;
	private String mobile;
	private LoginTypeEnum loginTypeEnum;
	private DeviceType deviceType;
	private String name;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public LoginTypeEnum getLoginTypeEnum() {
		return loginTypeEnum;
	}

	public void setLoginTypeEnum(LoginTypeEnum loginTypeEnum) {
		this.loginTypeEnum = loginTypeEnum;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
