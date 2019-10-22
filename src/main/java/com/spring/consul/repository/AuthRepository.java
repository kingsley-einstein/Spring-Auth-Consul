package com.spring.consul.repository;

import java.util.Optional;

import com.spring.consul.model.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
  Optional<Auth> findByUsername(String username);
}
