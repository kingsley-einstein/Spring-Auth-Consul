package com.spring.consul.errors;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String msg) {
    super(msg);
  }
}
