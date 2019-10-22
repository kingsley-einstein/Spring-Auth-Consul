package com.spring.consul.middlewares;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;
import com.spring.consul.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private AuthRepository repository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Authorization");
    if (requestTokenHeader == null) {
      throw new ServletException("Authorization header is null");
    }
    if (requestTokenHeader.startsWith("Bearer")) {
      String token = requestTokenHeader.substring(7);
      try {
        String username = jwtUtil.getSubjectFromToken(token);
        Auth auth = repository.findByUsername(username)
          .get();
        if (jwtUtil.tokenIsValid(token)) {
          Map<String, Object> credentials = new HashMap<>();
          credentials.put("username", auth.getUsername());
          credentials.put("password", auth.getPassword());

          UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(auth, credentials);
          uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(uPAT);
        } 
      } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException(exception.getMessage());
      } catch (ExpiredJwtException exception) {
        throw new ServletException(exception.getMessage());
      }
    }
    chain.doFilter(request, response);
  }
}
