package com.spring.consul.auth;

import com.spring.consul.auth.user.CustomUserDetailsService;
import com.spring.consul.middlewares.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private JwtAuthEntryPoint authEntryPoint;

  @Autowired
  private CustomUserDetailsService service;

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtRequestFilter filter() {
    return new JwtRequestFilter();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(service).passwordEncoder(encoder());
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
      .disable()
      .authorizeRequests()
      .antMatchers("/", "/actuator/health")
      .permitAll()
      .antMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .exceptionHandling()
      .authenticationEntryPoint(authEntryPoint)
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class);
  }
}
