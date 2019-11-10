package com.cw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cw.dao.EmployeeDao;
import com.cw.exception.CustomException;
import com.cw.model.Employee;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EmployeeViewController {

	@Autowired
	EmployeeDao dao;
	
	/*======== 查询功能 =========*/
	
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
	
	/*======== 添加功能 =========*/
	
	/**
	 * 打开雇员添加视图页面
	 * @return
	 */
	@GetMapping("/emp/add")
	public String employeeAddView() {
		return "employeeadd";
	}
	
	/**
	 * 雇员添加（提交）
	 * @param employee
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/emp/add")
	// springmvc会将form提交的数据自动封闭成javabean。但要求form属性与javabean对象中属性需一一对应，否则对应javabean属性为空。
	public String employeeAdd(Employee employee) {
		if(StringUtils.isEmpty(employee.getName())) {
			 throw new CustomException("雇员名称为空");
		}
		dao.save(employee);
		return "redirect:/employees";
	}
	
	/*======== 修改功能 =========*/
	
	/**
	 * 打开修改视图页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/emp/edit/{id}")
	public String editEmployeeView(
			@PathVariable(name = "id",required = false) String id,
			Model model
			) {
		/**
		 * Optional<T> findById(ID id)中Optional的一些用法：
		 * 1）如果没找到指定实体，则返回一个默认值。
		 * Foo foo = repository.findById(id).orElse(new Foo());
		 * 或者
		 * Foo foo = repository.findById(id).orElse(null);
		 * 2）如果找到实体返回它，否则抛出异常
		 * return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
		 * 3）假设希望根据是否找到实体来应用不同的处理（无需抛出异常）
		 * Optional<Foo> fooOptional = fooRepository.findById(id);
		 * if (fooOptional.isPresent()){
		 *    Foo foo = fooOptional.get();
		 *    // 处理 foo ...
		 * } else{ //另一种情况.... }
		 */
		// orElse() 方法：If a value is present, returns the value, otherwise returns other.
		Employee employee = dao.findById(id).orElse(new Employee());
		model.addAttribute("emp", employee);
		return "/employeeedit";
	}
	
	/**
	 * 提交要修改的数据
	 * @param employee
	 * @return
	 */
	@PutMapping("/emp/edit")
	public String editEmployee(Employee employee) {
		log.info("==="+employee.toString());
		dao.save(employee);
		return "redirect:/employees";
	}
	
	/*======== 删除功能 =========*/
	
	@DeleteMapping("/emp/del/{id}")
	public String deleteEmployee(
			@PathVariable("id") String id) {
		log.info(String.format("删除雇员ID为【%s】 的雇员", id));
		dao.deleteById(id);
		return "redirect:/employees";
	}
}
