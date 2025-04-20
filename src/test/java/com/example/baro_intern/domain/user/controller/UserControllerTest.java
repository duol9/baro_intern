package com.example.baro_intern.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.baro_intern.config.jwt.JwtUtil;
import com.example.baro_intern.domain.common.PasswordEncoder;
import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.entity.UserRole;
import com.example.baro_intern.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    private User saveUser(String username, String password, String nickname, UserRole role) {
        return userRepository.save(User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .nickname(nickname)
            .userRole(role)
            .build());
    }

    private String generateToken(User user) {
        return jwtUtil.createToken(user.getId(), user.getUsername(), user.getNickname(), user.getUserRole());
    }

    @Test
    @DisplayName("관리자는 다른 사용자에게 관리자 권한을 부여할 수 있다.")
    void admin_can_grant_admin_role() throws Exception {
        // given
        User user = saveUser("user", "user123", "유자", UserRole.USER);

        User admin = saveUser("admin", "admin123", "고라니자", UserRole.ADMIN);

        String token = generateToken(admin);

        // when & then
        mockMvc.perform(patch("/admin/users/" + user.getId() + "/roles")
                .header("Authorization",  token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.roles[0].role").value("ADMIN"));
    }

    @Test
    @DisplayName("일반 사용자는 관리자 권한 부여 요청 시 403 Forbidden을 반환한다.")
    void non_admin_cannot_grant_admin_role() throws Exception {
        // given
        User user = saveUser("user", "user123", "유자", UserRole.USER);

        User target = saveUser("target", "target123", "타겟", UserRole.ADMIN);

        String token = generateToken(target);

        // when & then
        mockMvc.perform(patch("/admin/users/" + target.getId() + "/roles")
                .header("Authorization", token))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 관리자 권한을 부여하려 하면 404가 반환된다.")
    void grant_admin_to_nonexistent_user() throws Exception {
        // given
        User admin = saveUser("admin", "admin123", "고라니자", UserRole.ADMIN);

        String token = generateToken(admin);
        // when & then
        mockMvc.perform(patch("/admin/users/9999/roles")
                .header("Authorization", token))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
    }
}