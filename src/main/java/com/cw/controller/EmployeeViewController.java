package com.cw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cw.dao.EmployeeDao;

@Controller
public class EmployeeViewController {

	@Autowired
	EmployeeDao dao;
	
	/**
	 * 雇员列表
	 * @param model
	 * @return 雇员列表页面
	 */
	@GetMapping("/employees")
	public String employees(Model model) {  
		// 以下3种类型的参数均可：
		// org.springframework.ui.Model
		// org.springframework.ui.ModelMap
		// java.util.Map
		model.addAttribute("emps", dao.findAll());  // 这里不定义Service类了，为了方便
		return "employees";
	}
	
	public void name() {
		
	}
}
