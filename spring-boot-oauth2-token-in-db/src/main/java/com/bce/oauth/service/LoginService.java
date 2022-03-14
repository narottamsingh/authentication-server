package com.bce.oauth.service;

import com.bce.model.LoginBO;
import com.bce.oauth.entity.Login;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
public interface LoginService {

	Login signup(LoginBO signupLogin);

	Login checkExistingUserByEmail(LoginBO login);

	Login checkExistingUserByMobile(LoginBO login);
}
