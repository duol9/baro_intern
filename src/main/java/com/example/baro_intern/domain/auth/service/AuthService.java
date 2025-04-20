package com.example.baro_intern.domain.auth.service;

import com.example.baro_intern.config.jwt.JwtUtil;
import com.example.baro_intern.domain.auth.dto.request.LoginRequest;
import com.example.baro_intern.domain.auth.dto.request.RegisterRequest;
import com.example.baro_intern.domain.auth.dto.response.LoginResponse;
import com.example.baro_intern.domain.auth.dto.response.RegisterResponse;
import com.example.baro_intern.domain.common.PasswordEncoder;
import com.example.baro_intern.domain.common.exception.CustomException;
import com.example.baro_intern.domain.common.exception.ExceptionType;
import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.entity.UserRole;
import com.example.baro_intern.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register (RegisterRequest registerRequest, UserRole userRole) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new CustomException(ExceptionType.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        User savedUser = userRepository.save(
            User.builder()
                .username(registerRequest.username())
                .nickname(registerRequest.nickname())
                .password(encodedPassword)
                .userRole(userRole)
                .build()
        );

        return RegisterResponse.from(savedUser);
    }

    public LoginResponse login (LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new CustomException(ExceptionType.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.createToken(
            user.getId(),
            user.getUsername(),
            user.getNickname(),
            user.getUserRole()
        );

        return new LoginResponse(token);
    }
}
