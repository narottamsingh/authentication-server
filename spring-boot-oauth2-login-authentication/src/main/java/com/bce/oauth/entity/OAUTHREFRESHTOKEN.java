package com.bce.oauth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "oauth_refresh_token")
public class OAUTHREFRESHTOKEN {

	@Id
	String tokenId;
	
	@Column(length = 100000)
	byte[] token;
	
	@Column(length = 100000)
	byte[] authentication;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public byte[] getToken() {
		return token;
	}
	public void setToken(byte[] token) {
		this.token = token;
	}
	public byte[] getAuthentication() {
		return authentication;
	}
	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}
	
	 

}
