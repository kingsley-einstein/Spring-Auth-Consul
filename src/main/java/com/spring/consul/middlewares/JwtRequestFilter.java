package com.spring.consul.middlewares;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.consul.auth.user.CustomUserDetailsService;
import com.spring.consul.errors.BadRequestException;
import com.spring.consul.errors.UnauthorizedException;
import com.spring.consul.model.Auth;
import com.spring.consul.repository.AuthRepository;
import com.spring.consul.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  private List<String> excludedUrls = Arrays.asList("/api/v1/auth/register", "/api/v1/auth/login", "/actuator/health");

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private AuthRepository repository;

  @Autowired
  private CustomUserDetailsService service;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Authorization");
    if (requestTokenHeader == null) {
      throw new UnauthorizedException("Authorization header not present in request");
    }
    if (requestTokenHeader.startsWith("Bearer")) {
      String token = requestTokenHeader.substring(7);
      try {
        String username = jwtUtil.getSubjectFromToken(token);
        Auth auth = repository.findByUsername(username)
          .get();
        // System.out.printf("User --- %s", auth.getUsername());
        if (jwtUtil.tokenIsValid(token)) {
          Map<String, Object> credentials = new HashMap<>();
          credentials.put("username", auth.getUsername());
          credentials.put("password", auth.getPassword());

          UserDetails userDetails = service.loadUserByUsername(auth.getUsername());

          // System.out.println(userDetails.getPassword());

          UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(userDetails, null);
          uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(uPAT);
        } 
      } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException(exception.getMessage());
      } catch (ExpiredJwtException exception) {
        throw new ServletException(exception.getMessage());
      }
    } else {
      throw new BadRequestException("Authorization must be of type 'Bearer'");
    }
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return excludedUrls.stream()
      .anyMatch((exclude) -> exclude.equalsIgnoreCase(request.getServletPath()));
  }
}
