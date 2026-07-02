package com.java.module.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.module.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);
}
