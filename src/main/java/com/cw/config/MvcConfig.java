package com.cw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cw.handler.DemoIntercetors;

/**
 * MVC开发者自己添加的配置
 * 不声明就使用自动配置，声明就覆盖自动配置中相关部分
 * @author cwly1
 *
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	DemoIntercetors demoIntercetors;
	
	/**
	 * mvc配置添加拦截器
	 * 拦截所有请求 /**
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		// WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(demoIntercetors).addPathPatterns("/**");
	}
	
	@Bean
	public LocaleResolver localeResolver(MyLocaleResolver myLocaleResolver) {
		return myLocaleResolver;
	}

}
