package com.spring.consul.errors;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
  public BadRequestException(String msg) {
    super(msg);
  }
}
