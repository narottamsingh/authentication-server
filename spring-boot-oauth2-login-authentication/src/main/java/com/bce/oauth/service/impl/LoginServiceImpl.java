package com.bce.oauth.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bce.enume.StatusCode;
import com.bce.model.LoginBO;
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

	@Autowired
	RestTemplate restTemplate;

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

	@Override
	public Login login(LoginBO login) {
		Login loginDB = this.loginRepository.findByEmailId(login.getEmailId());
		if (loginDB != null && checkPassword(loginDB.getPassword(), login.getPassword())) {
			return loginDB;
		} else {
			return null;
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

	@Override
	public int updateEmailStatus(Long id, StatusCode statusCode) {

		return this.loginRepository.updateLoginSetVerifyEmailForId(statusCode, id);
	}

	@Override
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

	@Override
	public ResponseEntity<Object> generateAuthToken(String username, String password) {
		try {
			String authentication = PROP_CLIENTID + ":" + PROP_SECRET;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(authentication.getBytes()));
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<String> requestEnty = new HttpEntity<>(headers);
			String URL = OAUTH_TOKEN_URL + "/oauth/token";
			URL += "?username=" + username;
			URL += "&grant_type=" + OAUTH_GRANT_TYPE;
			URL += "&password=" + password;

			ResponseEntity<Object> result = restTemplate.exchange(URL, HttpMethod.POST, requestEnty, Object.class);
			return result;
		} catch (RestClientException e) {

			e.printStackTrace();
			return null;
		}

	}

	@Override
	public ResponseEntity<Object> refreshAuthToken(String refresh_token) {
		try {
			String authentication = PROP_CLIENTID + ":" + PROP_SECRET;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(authentication.getBytes()));
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<String> requestEnty = new HttpEntity<>(headers);
			String URL = OAUTH_TOKEN_URL + "/oauth/token";
			URL += "?grant_type=" + OAUTH_REFRESH_TYPE;
			URL += "&refresh_token=" + refresh_token;

			ResponseEntity<Object> result = restTemplate.exchange(URL, HttpMethod.POST, requestEnty, Object.class);
			return result;
		} catch (RestClientException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public ResponseEntity<Object> logOut(String access_token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + access_token);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<String> requestEnty = new HttpEntity<>(headers);
			String URL = OAUTH_TOKEN_URL + "/oauth/logout";

			ResponseEntity<Object> result = restTemplate.exchange(URL, HttpMethod.GET, requestEnty, Object.class);
			return result;
		} catch (RestClientException e) {
			e.printStackTrace();
			return null;
		}

	}

}
