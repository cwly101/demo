package com.cw.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cw.model.User;

@Controller
public class DemoViewController {

	@Autowired 
	User user;
	
	@GetMapping("/thymeleaf")
	public String abcView(Map<String, Object> info) {
		info.put("author", "矛盾");
		info.put("user", user);
		System.out.println(user);
		return "thymeleaf";
	}
	
	@GetMapping("/demo")
	public String name() {
		return "demo";
	}
}
