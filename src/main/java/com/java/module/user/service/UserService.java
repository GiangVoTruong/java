package com.java.module.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.java.module.user.dto.UserResponse;
import com.java.module.user.entity.Users;
import com.java.module.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).collect(Collectors.toList());
    }

    public UserResponse getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserResponse.from(user);
    }
}
