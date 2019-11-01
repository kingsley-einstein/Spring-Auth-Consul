package com.spring.consul.middlewares.config;

import com.spring.consul.middlewares.JwtRequestFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {
  @Bean
  public FilterRegistrationBean<JwtRequestFilter> filterRegistrationBean() {
    FilterRegistrationBean<JwtRequestFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new JwtRequestFilter());
    bean.addUrlPatterns("/api/v1/getLoggedUser", "/api/v1/auth/update");
    return bean;
  }
}
