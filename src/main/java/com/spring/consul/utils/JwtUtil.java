package com.spring.consul.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.spring.consul.model.Auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final long JWT_TOKEN_VALIDITY = 60 * 2;

  @Value("{jwt.secret}")
  private String secret;

  public String generateToken(Map<String, Object> claims, Auth auth) {
    return Jwts.builder().setClaims(claims).setSubject(auth.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
      .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  private Claims getClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Date getExpirationDate(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getExpiration();
  }

  private Boolean tokenIsExpired(String token) {
    Date expiration = getExpirationDate(token);
    return expiration.before(new Date());
  }

  public String getSubjectFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject();
  }

  public Boolean tokenIsValid(String token) {
    return (!tokenIsExpired(token));
  }
}
