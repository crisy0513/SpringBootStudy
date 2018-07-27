package com.crisy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crisy.entity.Employee;
import com.crisy.service.EmployeeService;

/**
 * ����jpa
 * Title:EmployeeController
 * Description:
 * @author wangchenxin
 * @date 2018-7-27 ����9:00:11
 */
@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	//����ID����ʵ����
	@RequestMapping(value="/getEmp")
	public Employee loadEmployee(){
		Employee e = employeeService.findById(1l);
		return e;
	}
	//����
	@RequestMapping(value="/addEmp")
	public void add(){
		Employee e = new Employee();
		e.setDepartment(1l);
		e.setName("�ܲ�");
		employeeService.save(e);
	}
	//ɾ��
	@RequestMapping(value="/deleteEmp")
	public void delete(){
		employeeService.delete(1l);
	}
	//����
	@RequestMapping(value="/updateEmp")
	public void update(){
		employeeService.updateNameById(3l,"��˽�");
	}
}
