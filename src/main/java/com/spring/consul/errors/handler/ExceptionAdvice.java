package com.spring.consul.errors.handler;

import com.spring.consul.errors.BadRequestException;
import com.spring.consul.errors.NotFoundException;
import com.spring.consul.errors.UnauthorizedException;
import com.spring.consul.responses.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Response<String>> notFound(NotFoundException exception) {
    return new ResponseEntity<>(new Response<String>(404, exception.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<Response<String>> unauthorized(UnauthorizedException exception) {
    return new ResponseEntity<>(new Response<String>(401, exception.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Response<String>> badRequest(BadRequestException exception) {
    return new ResponseEntity<>(new Response<String>(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
