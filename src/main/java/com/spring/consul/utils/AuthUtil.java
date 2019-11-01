package com.spring.consul.utils;

import java.util.Optional;

import com.spring.consul.errors.UnauthorizedException;
import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
  @Autowired
  private AuthRepository repository;
  
  public AuthUtil() {}

  public Auth getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // System.out.printf("\n Auth --- %s", ((Auth) authentication.getPrincipal()).getUsername());
    UserDetails details = (UserDetails) authentication.getPrincipal();
    Optional<Auth> auth = repository.findByUsername(details.getUsername());
    if (!auth.isPresent()) {
      // Throw a 401
      System.out.println("--- User not authenticated ---");
      throw new UnauthorizedException("Unauthorized Request");
    }
    System.out.printf("Authenticated User --- %s", auth.get().getUsername());
    return auth.get();
  }
}
