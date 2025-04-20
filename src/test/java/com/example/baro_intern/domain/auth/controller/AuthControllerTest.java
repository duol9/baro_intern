package com.example.baro_intern.domain.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.baro_intern.domain.auth.dto.request.LoginRequest;
import com.example.baro_intern.domain.auth.dto.request.RegisterRequest;
import com.example.baro_intern.domain.common.PasswordEncoder;
import com.example.baro_intern.domain.common.exception.ExceptionType;
import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.entity.UserRole;
import com.example.baro_intern.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("사용자는 회원가입에 성공 할 수 있다.")
    void signup_success() throws Exception {
        // when
        RegisterRequest registerRequest = new RegisterRequest("tsetuser", "1234", "nick");

        // then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.username").value("user"))
            .andExpect(jsonPath("$.data.nickname").value("nick"))
            .andExpect(jsonPath("$.data.roles[0].role").value("USER"));
    }

    @Test
    @DisplayName("이미 존재하는 username으로 회원가입 시 400 에러가 발생한다.")
    void signup_duplicateUsername() throws Exception {
        // given
        userRepository.save(User.builder()
            .username("duplicateUser")
            .password(passwordEncoder.encode("password123"))
            .nickname("nick")
            .userRole(UserRole.USER)
            .build()
        );

        // when
        RegisterRequest request = new RegisterRequest("duplicateUser", "1234", "newnick");

        // then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("USER_ALREADY_EXISTS"));
    }


    @Test
    @DisplayName("사용자는 로그인에 성공할 수 있다.")
    void login_success() throws Exception {
        // given
        userRepository.save(User.builder()
            .username("testuser")
            .nickname("nickname")
            .password(passwordEncoder.encode("1234"))
            .userRole(UserRole.USER)
            .build());

        LoginRequest loginRequest = new LoginRequest("testuser", "1234");

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").exists())
            .andExpect(jsonPath("$.message").value("로그인 성공"));
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인에 실패하고 401을 반환한다.")
    void login_invalidPassword() throws Exception {
        // given
        userRepository.save(User.builder()
            .username("testuser")
            .nickname("nickname")
            .password(passwordEncoder.encode("1234"))
            .userRole(UserRole.USER)
            .build());

        LoginRequest loginRequest = new LoginRequest("testuser", "fail");

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(ExceptionType.INVALID_CREDENTIALS.getCode()))
            .andExpect(jsonPath("$.message").value(ExceptionType.INVALID_CREDENTIALS.getMessage()));
    }
}