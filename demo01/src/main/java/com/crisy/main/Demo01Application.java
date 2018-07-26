package com.crisy.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.crisy.*"})
public class Demo01Application extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(Demo01Application.class, args);
	}
}
