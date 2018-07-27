package com.crisy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crisy.entity.User;

@RestController
public class UserController {
	/**
	 * 测试@RestController 返回JSON
	 * @return {"id":1,"name":"张飞","age":22,"school":"三国"}
	 */
	@RequestMapping(value = "/getUser")
	public User getUser(){
		User u = new User();
		u.setAge(22);
		u.setId(1l);
		u.setName("张飞");
		u.setSchool("三国");
		return u;
	}
}
