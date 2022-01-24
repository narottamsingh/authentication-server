package com.bce.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bce.model.UserInfo;
import com.bce.service.UserInfoService;

@RestController
public class UserController {
	@Autowired
	private UserInfoService userService;

	@GetMapping("/user/get")
	public List<UserInfo> getAllUser(@RequestHeader HttpHeaders requestHeader) {
		List<UserInfo> userInfos = userService.getAllActiveUserInfo();
		
		return userInfos;
	}

	@PostMapping("/user")
	public UserInfo addUser(@RequestBody UserInfo userRecord) {
		return userService.addUser(userRecord);
	}

}
