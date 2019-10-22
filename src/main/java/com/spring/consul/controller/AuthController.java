package com.spring.consul.controller;

import java.util.HashMap;
import java.util.Map;

import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;
import com.spring.consul.responses.Response;
import com.spring.consul.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
  @Autowired
  private AuthRepository repository;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/auth")
  @ResponseBody
  public ResponseEntity<Response<Map<String, Object>>> create(@RequestBody Auth body) {
    Auth auth = repository.save(body);
    Map<String, Object> mappedBody = new HashMap<>();
    mappedBody.put("id", auth.getId());
    mappedBody.put("username", auth.getUsername());
    mappedBody.put("token", jwtUtil.generateToken(new HashMap<>(), auth));

    Response<Map<String, Object>> response = new Response<>(201, mappedBody);
    ResponseEntity<Response<Map<String, Object>>> mainResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
    return mainResponse;
  }
}
