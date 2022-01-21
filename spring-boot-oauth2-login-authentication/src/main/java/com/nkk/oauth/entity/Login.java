package com.nkk.oauth.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.nkk.enume.LoginTypeEnum;
import com.nkk.enume.StatusCode;

@Entity(name = "login")
public class Login extends CreateUpdateDate implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8371986559174369432L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "password")
	private String password;

	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "login_type")
	@Enumerated(EnumType.STRING)
	private LoginTypeEnum loginTypeEnum;

	@Column(name = "login_status")
	@Enumerated(EnumType.STRING)
	private StatusCode loginStatus;

	@Column(name = "verify_email")
	@Enumerated(EnumType.STRING)
	private StatusCode verifyEmail;
	
	@Column(name = "verify_mobile")
	@Enumerated(EnumType.STRING)
	private StatusCode verifyMobile;
		
	@Column(name = "last_login_time")
	private Date lastLoginTime;

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

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

	public StatusCode getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(StatusCode loginStatus) {
		this.loginStatus = loginStatus;
	}

	public StatusCode getVerifyEmail() {
		return verifyEmail;
	}

	public void setVerifyEmail(StatusCode verifyEmail) {
		this.verifyEmail = verifyEmail;
	}

	public StatusCode getVerifyMobile() {
		return verifyMobile;
	}

	public void setVerifyMobile(StatusCode verifyMobile) {
		this.verifyMobile = verifyMobile;
	}

	
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	
}
