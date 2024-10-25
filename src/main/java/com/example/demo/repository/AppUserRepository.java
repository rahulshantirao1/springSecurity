package com.example.demo.repository;

import com.example.demo.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

   public Optional<AppUser> findByUsername(String username);
  public   Optional<AppUser>findByEmail(String email);
}