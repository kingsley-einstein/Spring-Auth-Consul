package com.spring.consul.controller;

import java.util.HashMap;
import java.util.Map;

import com.spring.consul.errors.BadRequestException;
import com.spring.consul.errors.NotFoundException;
import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;
import com.spring.consul.responses.Response;
import com.spring.consul.utils.AuthUtil;
import com.spring.consul.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @Autowired
  private AuthUtil authUtil;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @PostMapping("/auth/register")
  @ResponseBody
  public ResponseEntity<Response<Map<String, Object>>> create(@RequestBody Auth body) {
    if (repository.findByUsername(body.getUsername()).isPresent()) {
      throw new BadRequestException("Username already in use.");
    }
    Auth auth = repository.save(body);
    Map<String, Object> mappedBody = new HashMap<>();
    mappedBody.put("id", auth.getId());
    mappedBody.put("username", auth.getUsername());
    mappedBody.put("token", jwtUtil.generateToken(new HashMap<>(), auth));

    Response<Map<String, Object>> response = new Response<>(201, mappedBody);
    ResponseEntity<Response<Map<String, Object>>> mainResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
    return mainResponse;
  }

  @PostMapping("/auth/login")
  @ResponseBody
  public ResponseEntity<Response<Map<String, Object>>> login(@RequestBody Auth body) {
    Auth auth = repository.findByUsername(body.getUsername())
      .orElseThrow(() -> new NotFoundException("User with that username not found"));
    if (!(encoder.matches(body.getPassword(), auth.getPassword()))) {
      throw new BadRequestException("Incorrect password");
    }
    Map<String, Object> mappedBody = new HashMap<>();
    mappedBody.put("username", auth.getUsername());
    mappedBody.put("id", auth.getId());
    mappedBody.put("token", jwtUtil.generateToken(new HashMap<>(), auth));
    return new ResponseEntity<>(new Response<>(200, mappedBody), HttpStatus.OK);
  }

  @PutMapping("/auth/update")
  @ResponseBody
  public ResponseEntity<Response<Map<String, Object>>> update(@RequestBody Map<String, Object> body) {
    Auth auth = authUtil.getAuthenticatedUser();
    Auth update = repository.findById(auth.getId()).get();
    body.keySet().forEach((key) -> {
      if (key.equals("password")) {
        update.setPassword((String) body.get("password"));
      }
    });
    Auth updated = repository.save(update);
    Map<String, Object> mappedBody = new HashMap<>();
    mappedBody.put("username", updated.getUsername());
    mappedBody.put("id", updated.getId());
    return new ResponseEntity<>(new Response<>(200, mappedBody), HttpStatus.OK);
  }

  @GetMapping("/getLoggedUser")
  @ResponseBody
  public ResponseEntity<Response<Auth>> loggedUser() {
    Auth auth = authUtil.getAuthenticatedUser();
    return new ResponseEntity<>(new Response<>(200, auth), HttpStatus.OK);
  }
}
