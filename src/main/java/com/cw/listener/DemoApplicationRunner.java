package com.cw.listener;

import java.util.ArrayList;
//import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.cw.dao.EmployeeDao;
import com.cw.model.Employee;

import lombok.extern.slf4j.Slf4j;

/**
 * 程序初始化类
 * ioc容器初始化完成各个组件都加载完毕之后，执行此bean
 * @author cwly1
 *
 */
@Component
@Slf4j
public class DemoApplicationRunner implements ApplicationRunner {   // 更多初始化方式见：https://www.jb51.net/article/145143.htm

	// ApplicationRunner比CommandLineRunner优先级高，先执行。
	/**
	 * SpringApplication.run(DemoApplication.class, args); 的run方法源码片段：
	 * // 从ioc容器中获取所有ApplicationRunner和CommandLineRunner方法进行回调
	 * afterRefresh(context, applicationArguments);
	 * 源码如下：
	 * private void callRunners(ApplicationContext context, ApplicationArguments args) {
		List<Object> runners = new ArrayList<>();
		runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
		runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
		AnnotationAwareOrderComparator.sort(runners);
		for (Object runner : new LinkedHashSet<>(runners)) {
			if (runner instanceof ApplicationRunner) {
				callRunner((ApplicationRunner) runner, args);
			}
			if (runner instanceof CommandLineRunner) {
				callRunner((CommandLineRunner) runner, args);
			}
		}
	}
	 */
	
	/**
	 * 
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
//		init();  // 数据已经在mongodb数据库了。仅首次创建时用一次！
		log.info("=== DemoApplicationRunner done! ===");
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
