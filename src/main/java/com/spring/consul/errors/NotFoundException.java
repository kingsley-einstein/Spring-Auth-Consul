package com.spring.consul.errors;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
  public NotFoundException(String msg) {
    super(msg);
  }
}
