package com.bce.oauth.service.impl;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bce.model.LoginBO;
import com.bce.model.StatusCode;
import com.bce.oauth.entity.Login;
import com.bce.oauth.repository.LoginRepository;
import com.bce.oauth.service.LoginService;

/**
 * @author NAROTTAMS
 * @created_date Sep 20, 2020
 * @modify_by NAROTTAMS
 * @modify_time
 */
@Service
public class LoginServiceImpl implements LoginService, UserDetailsService {

	@javax.annotation.Resource
	private LoginRepository loginRepository;

	@Value("${authentication.oauth.token.url:http://localhost:8080}")
	private String OAUTH_TOKEN_URL;

	@Value("${authentication.oauth.clientid:springbootrest}")
	private String PROP_CLIENTID;

	@Value("${authentication.oauth.secret:restservice}")
	private String PROP_SECRET;

	@Value("${authentication.oauth.grant.type:password}")
	private String OAUTH_GRANT_TYPE;

	@Value("${authentication.oauth.grant.refresh.type:password}")
	private String OAUTH_REFRESH_TYPE;

	@Override
	public Login signup(LoginBO signupLogin) {
		Login login = checkExistingUserByEmail(signupLogin);
		if (login != null) {
			login.setLoginStatus(StatusCode.ALREADY_EXIST);
			return login;
		}
		{
			login = new Login();

			login.setPassword(new BCryptPasswordEncoder().encode(signupLogin.getPassword()));
			login.setLoginStatus(StatusCode.VERIFICATION_PENDING);
			if (signupLogin.getEmailId() != null) {
				login.setVerifyEmail(StatusCode.VERIFICATION_PENDING);
				login.setEmailId(signupLogin.getEmailId());
			}
			if (signupLogin.getMobile() != null) {
				login.setVerifyMobile(StatusCode.VERIFICATION_PENDING);
				login.setMobile(signupLogin.getMobile());
			}
			login.setLoginTypeEnum(signupLogin.getLoginTypeEnum());
			login.setCreatedTime(new Date());
			Login lDB = this.loginRepository.save(login);

			return lDB;
		}
	}


	public boolean checkPassword(String dbPassword, String userPass) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean isPasswordMatch = passwordEncoder.matches(userPass, dbPassword);
		return isPasswordMatch;
	}

	@Override
	public Login checkExistingUserByEmail(LoginBO login) {
		return this.loginRepository.findByEmailId(login.getEmailId());
	}

	@Override
	public Login checkExistingUserByMobile(LoginBO login) {
		return this.loginRepository.findByMobile(login.getMobile());
	}

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Login userInfo = null;
		if (userName.indexOf("@") != -1) {
			userInfo = loginRepository.findByEmailId(userName);
			GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getLoginTypeEnum().name());
			return new User(userInfo.getEmailId(), userInfo.getPassword(), Arrays.asList(authority));
		} else {
			userInfo = loginRepository.findByMobile(userName);
			GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getLoginTypeEnum().name());
			return new User(userInfo.getMobile(), userInfo.getPassword(), Arrays.asList(authority));
		}

	}


}
