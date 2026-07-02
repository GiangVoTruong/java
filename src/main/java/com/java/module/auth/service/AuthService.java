package com.java.module.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.java.module.auth.dto.LoginReponse;
import com.java.module.auth.dto.LoginRequest;
import com.java.module.auth.dto.RegisterRequest;
import com.java.module.user.dto.UserResponse;
import com.java.module.user.entity.Users;
import com.java.module.user.repository.UserRepository;
import com.java.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginReponse login(LoginRequest loginRequest) {
        Users user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        String accessToken = jwtService.generateToken(user.getUsername(), user.getEmail(), user.getPhone());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getEmail(), user.getPhone());
        return new LoginReponse(accessToken, refreshToken);
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return UserResponse.from(userRepository.save(user));
    }
}
