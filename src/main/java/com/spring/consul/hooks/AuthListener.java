package com.spring.consul.hooks;

import javax.persistence.PrePersist;

import com.spring.consul.model.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthListener {
  @Autowired
  private BCryptPasswordEncoder encoder;

  @PrePersist
  public void onSave(Auth auth) {
    // Hash password before save
    String password = encoder.encode(auth.getPassword());
    auth.setPassword(password);
  }
}