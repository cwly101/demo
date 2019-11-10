package com.cw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Employee {

	@Id
	private String id;  // mongodb数据库为_id，这里直接使用id即可。如果使用_id，会导致表单提交数据_id为null.
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
