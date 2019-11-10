package com.cw.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cw.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 * @author cwly1
 *
 */
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {

	/*======== 不具备自适应效果，无法对浏览器或其它客户端访问进行区别对待，返回不同数据格式 =========*/
	
	/*
	 * @ResponseBody
	 * 
	 * @ExceptionHandler(CustomException.class) public Map<String, Object>
	 * handleException(Exception e) { Map<String, Object> map = new HashMap<>();
	 * map.put("code", "100"); map.put("msg", e.getMessage());
	 * log.info(e.getMessage()); return map; }
	 */
	
	/*======== 自适应效果并携带自己的数据，浏览器或其它客户端访问进行区别对待，返回不同数据格式 =========*/
	
	@ExceptionHandler(CustomException.class)
	public String handleException(
			Exception e,
			HttpServletRequest request
			) {
		Map<String, Object> map = new HashMap<>();
		// 传入自己的错误状态码。4xx或5xx，否则不会进行错误页面。 状态码key，参见源码，如下：
		// Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		request.setAttribute("javax.servlet.error.status_code", 500);
		map.put("code", "100");
		map.put("message", e.getMessage().toString()+"aaa");  
		request.setAttribute("ext", map);  // 将自定义的额外扩展错误提示属性添加到请求属性中
		log.info(e.getMessage());
		// 转发到/error， 这里springboot进行了自适应配置
		return "forward:/error";
	}
}
