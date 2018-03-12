package com.citic.modules.sys.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class LogConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		super.addInterceptors(registry);
		registry.addInterceptor(new LogInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/sys/log/list")
		;
		
	}
	
}
