package com.crisy.service;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.crisy.entity.Employee;



@Transactional
public interface EmployeeService extends JpaRepository<Employee, Long>{
	//根据方法名来自动的生成SQL
	Employee findById(Long id);
	
	//修改 一定要加@Transactional和 @Modifying
	@Modifying  
	@Query(value = "update employee set name = :name where id = :id",nativeQuery = true)  
    void updateNameById(@Param("id") Long id, @Param("name") String name);
}
