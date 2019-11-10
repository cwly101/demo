package com.cw.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cw.model.User;

@Controller
public class DemoViewController {

	@Autowired 
	User user;
	
	@GetMapping("/thymeleaf")
	public String abcView(Map<String, Object> info) {
		info.put("author", "矛盾");
		info.put("user", user);
		return "thymeleaf";
	}
	
	/**
	 * 国际化语言测试处理器（解析一个登录页面模板）
	 * @return
	 */
	@GetMapping("/demo")
	public String name() {
		return "demo";
	}
	
	@PostMapping(value = "/login")
	public String login(
			// 表单中必须携带@RequestParam注解设置参数，名称需要一致
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String passowrd,
			Map<String, Object> map,
			HttpSession session
			) {
		if(!StringUtils.isEmpty(username) && passowrd.equals("123456")) {  // 登录成功
			session.setAttribute("login_user", username);  // 将用户放入session中. 拦截器验证使用
			return "redirect:/thymeleaf";  // 路由重定向
		}
		map.put("msg", "账号或密码错误");
		return "demo";
	}
}
