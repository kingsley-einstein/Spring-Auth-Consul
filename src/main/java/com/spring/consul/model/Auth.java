package com.spring.consul.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class Auth implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "Username is required")
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @NotEmpty
  @Column(name = "password", nullable = false)
  private String password;

  public Auth() {}

  public Auth(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getId() {
    return id;
  }
}
