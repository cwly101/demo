package com.cw;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cw.dao.EmployeeDao;
import com.cw.model.Employee;

import lombok.extern.slf4j.Slf4j;

/**
 * 程序初始化类
 * @author cwly1
 *
 */
@Component
@Slf4j
public class AppInit implements ApplicationRunner {   // 更多初始化方式见：https://www.jb51.net/article/145143.htm

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		init();  // 数据已经在mongodb数据库了。仅首次创建时用一次！
		log.info("=== Appliction init done! ===");
	}

	@Autowired 
	EmployeeDao dao;
	
	public void init() {
		Employee tom = new Employee("No.300","Tom",25,"程序员");
		Employee jack = new Employee("No.301","Jack",30,"销售经理");
		Employee will = new Employee("No.302","Will",36,"项目经理");
		Employee Lilith = new Employee("No.303","Lilith",33,"美工");
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(tom);
		employees.add(jack);
		employees.add(will);
		employees.add(Lilith);
		
		dao.saveAll(employees);
		System.out.println("=== 初始化数据完成 ===");
	}
	
}
