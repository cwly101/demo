package com.cw.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cw.model.User;

import lombok.extern.log4j.Log4j2;
//import lombok.extern.slf4j.Slf4j;

@RestController
//@Slf4j
@Log4j2
public class DemoController {

	/**
	 * 获取初始化完成后组件User对象在容器中的数据（就是读取的yml配置文件中user前缀对应的数据）
	 */
	@Autowired
	User user;
	
	@GetMapping("/")
	public User test() {
//		System.out.println("test request");
		log.info(String.format("==== test request , current time: %s ====", new Date()));
//		User user = new User();
//		user.setName("tomson");
//		user.setAge(22);
		System.out.println(user.toString());
		return user;
	}
}
