package com.cw.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 自定义区域信息解析器
 * @author cwly1
 *
 */
@Component
public class MyLocaleResolver implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String localeInfo = request.getParameter("l");
		// Locale locale = Locale.getDefault();  // 默认为系统Locale区域信息
		Locale locale = null;
		if(!StringUtils.isEmpty(localeInfo)) {  // 如果有请求携带的Locale信息，使用携带的
			String[] split = localeInfo.split("_");  //如：zh_CN 、en_US 等
			locale = new Locale(split[0], split[1]);
		}else {  // 如果没有携带，由浏览器设置语言决定
			String header_language = request.getHeader("Accept-Language");
			String l = header_language.substring(0, header_language.indexOf(","));
			String[] split = l.split("-");
			locale = new Locale(split[0], split[1]);
		}
		
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub
		
	}

}
