package com.crisy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crisy.entity.User;

@RestController
public class UserController {
	/**
	 * ����@RestController ����JSON
	 * @return {"id":1,"name":"�ŷ�","age":22,"school":"����"}
	 */
	@RequestMapping(value = "/getUser")
	public User getUser(){
		User u = new User();
		u.setAge(22);
		u.setId(1l);
		u.setName("�ŷ�");
		u.setSchool("����");
		return u;
	}
}
