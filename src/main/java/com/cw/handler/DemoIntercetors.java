package com.cw.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求拦截器
 * @author cwly1
 *
 */
@Component
@Slf4j
public class DemoIntercetors implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info(String.format("=== request url: %s ===", request.getRequestURI()));
		String queryString = request.getQueryString();
		log.info(String.format("query string:", queryString));
		log.info("IP:"+request.getRemoteAddr());
		// return HandlerInterceptor.super.preHandle(request, response, handler);
		return true;
	}

}
