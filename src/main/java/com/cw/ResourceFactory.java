package com.cw;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/**
 * 用于读取自定义的.yml文件配置信息
 * 说明：使@PropertySource注解能够读取.yml配置文件，默认只能读取.properties配置文件
 * @author cwly1
 *
 */
public class ResourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		 String sourceName = (name == null) ? resource.getResource().getFilename() : name;
	        assert sourceName != null;
	        if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
	            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
	            factory.setResources(resource.getResource());
	            factory.afterPropertiesSet();
	            Properties properties = factory.getObject();
	            assert properties != null;
	            return new PropertiesPropertySource(sourceName, properties);
	        }
	        return super.createPropertySource(name, resource);
	}
}
