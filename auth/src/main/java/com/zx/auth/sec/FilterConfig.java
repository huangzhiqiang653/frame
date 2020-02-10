package com.zx.auth.sec;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @program: FilterConfig
 * @description: 过滤器配置类
 * @author: hmchen
 * @create: 2020年2月7日16:41:22
 **/
@Configuration
public class FilterConfig {

    @Resource
    LoginFilter loginFilter;

    @Bean
    public FilterRegistrationBean registerAuthFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean<>();
        registration.setFilter(loginFilter);
        //过滤规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("loginFilter");
        //过滤器顺序
        registration.setOrder(1);
        return registration;
    }
}
