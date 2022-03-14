package com.bce.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bce.enume.StatusCode;
import com.bce.model.CustomResponse;
import com.bce.model.LoginBO;
import com.bce.oauth.entity.Login;
import com.bce.oauth.service.LoginService;


@RestController
@RequestMapping("/login")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@javax.annotation.Resource
	private LoginService loginService;

	@RequestMapping(value = "/logoutuser", method = RequestMethod.GET)
	public ResponseEntity<CustomResponse<Login>> logout(@RequestParam("access_token") String access_token) {
		loginService.logOut(access_token);
		CustomResponse<Login> customResponse = new CustomResponse<Login>(null);
		customResponse.setMessage("Logout Successfully");
		customResponse.setStatus("SUCCESS");
		return new ResponseEntity<>(customResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/loginuser", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse<Login>> login(@RequestBody LoginBO login) {
		log.info("user enter in login()..");
		Login loginDB = loginService.login(login);

		if (loginDB != null) {
			ResponseEntity<Object> result = loginService.generateAuthToken(login.getEmailId(), login.getPassword());
			if (result != null && result.getStatusCode() == HttpStatus.OK) {
				CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
				customResponse.setMessage("Login Successfully");
				customResponse.setStatus("SUCCESS");
				customResponse.setToken(result.getBody());
				loginDB.setPassword(null);
				customResponse.setData(loginDB);
				return new ResponseEntity<>(customResponse, HttpStatus.OK);
			} else {
				CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
				customResponse.setMessage("Authentication failed");
				customResponse.setStatus("UNAUTHORIZED");
				customResponse.setData(null);
				return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);

			}
		} else {
			CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
			customResponse.setMessage("Detail does not match");
			customResponse.setStatus("FAIL");
			return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse<Login>> signup(@RequestBody LoginBO login) {
		log.info("user enter in signup()..");
		Login loginDB = loginService.signup(login);

		if (loginDB != null && loginDB.getLoginStatus() != StatusCode.ALREADY_EXIST) {
			ResponseEntity<Object> result = loginService.generateAuthToken(login.getEmailId(), login.getPassword());
			if (result != null && result.getStatusCode() == HttpStatus.OK) {

				CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
				loginDB.setPassword(null);
				customResponse.setData(loginDB);
				customResponse.setMessage("User created successfully");
				customResponse.setStatus("CREATED");
				customResponse.setToken(result.getBody());
				return new ResponseEntity<>(customResponse, HttpStatus.CREATED);
			} else {
				CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
				customResponse.setMessage("Authentication failed");
				customResponse.setStatus("UNAUTHORIZED");
				return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);

			}
		} else {
			CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
			customResponse.setMessage("Provided details al-ready exist");
			customResponse.setStatus("ALREADY");
			customResponse.setData(null);
			return new ResponseEntity<>(customResponse, HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/refreshtoken", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse<Login>> refreshOauthToken(
			@RequestParam("refresh_token") String refresh_token) {
		ResponseEntity<Object> result = loginService.refreshAuthToken(refresh_token);
		if (result != null && result.getStatusCode() == HttpStatus.OK) {
			CustomResponse<Login> customResponse = new CustomResponse<Login>(null);
			customResponse.setMessage("Oauth token successfully refreshed");
			customResponse.setStatus("SUCCESS");
			customResponse.setToken(result.getBody());
			return new ResponseEntity<>(customResponse, HttpStatus.OK);
		} else {
			CustomResponse<Login> customResponse = new CustomResponse<Login>(null);
			customResponse.setMessage("Authentication failed");
			customResponse.setStatus("UNAUTHORIZED");
			customResponse.setToken(result.getBody());
			return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);

		}
	}

}
