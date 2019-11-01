package com.spring.consul.auth.user;

import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private AuthRepository repository;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      Auth auth = repository.findByUsername(username).get();
      AuthPrincipal principal = AuthPrincipal.create(auth);
      return principal;
    } catch (UsernameNotFoundException exception) {
      throw new UsernameNotFoundException("User with username not found");
    }
  }
}
