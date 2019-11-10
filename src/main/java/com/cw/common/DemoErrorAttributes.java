package com.cw.common;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * 注入自定义的错误属性
 * 说明：自适应效果并携带自己的数据，浏览器或其它客户端访问进行区别对待，返回不同数据格式
 * @author cwly1
 *
 */
@Component
public class DemoErrorAttributes extends DefaultErrorAttributes {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		errorAttributes.put("company", "cw");   // 添加自定义错误标识 
//		errorAttributes.remove("trace");   //移除系统提交的不想要的错误属性，如trace
		
		/**
		 * 源码：
		 * Object getAttribute(String name, int scope);
		 * int SCOPE_REQUEST = 0;
		 * int SCOPE_SESSION = 1;
		 * 这里我们使用的是 request域的数据
		 */
		Map<String, Object> ext = (Map<String, Object>) webRequest.getAttribute("ext", 0);  // 读取在全局异常处理器中设置的错误提示扩展属性。
		errorAttributes.put("ext", ext);
		return errorAttributes;
	}

}
