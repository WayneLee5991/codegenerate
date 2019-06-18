/**
 * 
 */
package com.yuyi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 默认页面设置，直接请求 ip：端口 可以直接跳转至指定页面
 * @author 李文庆
 * 2018年4月24日 下午1:42:03
 */
@Configuration
public class DefaultPageConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName( "forward:/index.html" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
	}
	
	private final String ORIGINS[] = new String[] {"OPTIONS", "GET", "POST", "PUT", "DELETE" };
	/**
	 * 解决跨域问题
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods(ORIGINS);
    }
	
	
}
