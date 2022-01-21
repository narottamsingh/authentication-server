package com.nkk.oauth.service;

import org.springframework.http.ResponseEntity;

import com.nkk.enume.StatusCode;
import com.nkk.model.LoginBO;
import com.nkk.oauth.entity.Login;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
public interface LoginService {

	Login signup(LoginBO signupLogin);

	Login login(LoginBO login);

	ResponseEntity<Object> generateAuthToken(String username, String password);

	ResponseEntity<Object> refreshAuthToken(String refresh_token);
	
	ResponseEntity<Object> logOut(String access_token);

	Login checkExistingUserByEmail(LoginBO login);

	Login checkExistingUserByMobile(LoginBO login);

	int updateEmailStatus(Long id, StatusCode statusCode);
}
