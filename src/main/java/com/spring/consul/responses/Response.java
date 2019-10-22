package com.spring.consul.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class Response<T> implements Serializable {
  @JsonProperty(value = "statusCode")
  private Integer statusCode;

  @JsonProperty(value = "body")
  private T body;

  public Response() {}

  public Response(Integer statusCode, T body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public T getBody() {
    return body;
  }
}
