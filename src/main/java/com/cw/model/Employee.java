package com.cw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Employee {

	@Id
	private String _id;
	/**
	 * 员工编号
	 */
	private String emp_no;
	private String name;
	private int age;
	/**
	 * 职务
	 */
	private String job;
	
	public Employee() {}
	
	public Employee(String emp_no,String name,int age,String job) {
		this.emp_no = emp_no;
		this.name = name;
		this.age = age;
		this.job = job;
	}
}
