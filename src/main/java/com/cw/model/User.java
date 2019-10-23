package com.cw.model;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.cw.ResourceFactory;

import lombok.Data;

@Component
@PropertySource(value = "classpath:user.yml", factory = ResourceFactory.class) //读取.yml配置文件，见：https://www.jianshu.com/p/632dce1281cc
//@PropertySource(value = "classpath:user.properties") // 默认只能读取.properties配置文件
@Data
@ConfigurationProperties(prefix = "user")  // 读取配置yml配置文件对应数据，映射到对象。默认只能读取全局配置文件数据。
@Validated
public class User {
	
	@NotEmpty(message = "用户名必须有")
	private String username;
	@Range(min = 1,max = 150,message = "年龄范围为1~150岁")
	private int age;
	
	private Map<String, Double> score;
	
	private List<Pet> pets;   // 自定义对象列表读取 （.yml 配置方式支持。.properties不支持）
//	private List<Integer> pets;   // .properties 将一切都看成是键值对方式，而value必须是它能理解的常规类型。List<Pet> 这种类型我试了多次，不行。
	
//	private Pet pet;
	
}
