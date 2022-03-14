package com.bce.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bce.model.CustomResponse;
import com.bce.model.LoginBO;
import com.bce.model.StatusCode;
import com.bce.oauth.entity.Login;
import com.bce.oauth.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@javax.annotation.Resource
	private LoginService loginService;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse<Login>> signup(@RequestBody LoginBO login) {
		log.info("user enter in signup()..");
		Login loginDB = loginService.signup(login);

		if (loginDB != null && loginDB.getLoginStatus() != StatusCode.ALREADY_EXIST) {

			CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
			loginDB.setPassword(null);
			customResponse.setData(loginDB);
			customResponse.setMessage("User created successfully");
			customResponse.setStatus("CREATED");
			return new ResponseEntity<>(customResponse, HttpStatus.CREATED);
		} else {
			CustomResponse<Login> customResponse = new CustomResponse<Login>(loginDB);
			customResponse.setMessage("Provided details al-ready exist");
			customResponse.setStatus("ALREADY");
			customResponse.setData(null);
			return new ResponseEntity<>(customResponse, HttpStatus.CONFLICT);
		}
	}

}
