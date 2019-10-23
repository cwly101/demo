package com.cw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // spring boot 应用入口注解。内部包含@EnableAutoConfiguration、@AutoConfigurationPackage
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("==== springboot application [deme] started! ====");
	}
	
	/**
	 * 注：打包成 jar 的时候使用的是内嵌的 web容器（这里使用的是undertow)，
	    server.port 只是设置了这个内嵌的 web容器的端口。 换成 war 这个内嵌的 
		web容器就不会运行了，自然端口号是外部容器决定的。
		
		jar与war：
		一般将项目分为两层：服务层和表现层（视图层），通常我们把服务层打包成jar，而把视图层的包打成war包。
		使用了springboot后，项目都会被打包成jar，或者打包成war部署在外部容器中也可以。
		RESTful API编写的后端服务打包成jar 运行，而前端UI界面不一定非得使用war包的外部服务器部署，UI界面本身属于
		静态资源文件的一种，可选择的服务器非常多。
		
		
		lombok使用：
		1. 将lombok插件安装到STS中。（ 下载最新的lombok插件，打开命令行，切换到lombok所有目录，执行: java -jar lombok.jar )
		2. 在pom.xml中添加lombok依赖
	 */

}
