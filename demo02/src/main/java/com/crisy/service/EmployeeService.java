package com.crisy.service;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.crisy.entity.Employee;



@Transactional
public interface EmployeeService extends JpaRepository<Employee, Long>{
	//���ݷ��������Զ�������SQL
	Employee findById(Long id);
	
	//�޸� һ��Ҫ��@Transactional�� @Modifying
	@Modifying  
	@Query(value = "update employee set name = :name where id = :id",nativeQuery = true)  
    void updateNameById(@Param("id") Long id, @Param("name") String name);
}
