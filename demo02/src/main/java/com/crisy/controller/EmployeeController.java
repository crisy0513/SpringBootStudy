package com.crisy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crisy.entity.Employee;
import com.crisy.service.EmployeeService;

/**
 * 测试jpa
 * Title:EmployeeController
 * Description:
 * @author wangchenxin
 * @date 2018-7-27 上午9:00:11
 */
@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	//根据ID加载实体类
	@RequestMapping(value="/getEmp")
	public Employee loadEmployee(){
		Employee e = employeeService.findById(1l);
		return e;
	}
	//保存
	@RequestMapping(value="/addEmp")
	public void add(){
		Employee e = new Employee();
		e.setDepartment(1l);
		e.setName("曹操");
		employeeService.save(e);
	}
	//删除
	@RequestMapping(value="/deleteEmp")
	public void delete(){
		employeeService.delete(1l);
	}
	//更新
	@RequestMapping(value="/updateEmp")
	public void update(){
		employeeService.updateNameById(3l,"猪八戒");
	}
}
