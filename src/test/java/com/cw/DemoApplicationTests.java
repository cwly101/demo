package com.cw;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class DemoApplicationTests {

	@Test
	void contextLoads() {

	}

	/**
	 * 传统日志记录方式
	 * 采用slf4j
	 */
	@Test
	void logging() {
		// Ctrl+2 快速生成变量
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("===log===");
		logger.debug("=== debug ===");
	}
	
	/**
	 * 采用lombok的日志记录方式
	 */
	@Test
	void lombokLogging() {
		log.info("=== lombok log4j2 log ===");
		log.trace("=== lombok log4j2 trace 跟踪信息 ===");
		log.debug("=== lombok log4j2 debug 调试信息 ===");
		log.error("=== lombok log4j2 Error  ===");
		// trace<debug<info<warn<error
		// springboot 默认打印info及以上级别日志信息
	}

}
