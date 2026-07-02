package com.java.module.user.dto;

import com.java.module.user.entity.Users;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String phone;

    public static UserResponse from(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
