package com.example.baro_intern.domain.auth.dto.response;

import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.entity.UserRole;
import java.util.List;

public record RegisterResponse(
    String username,
    String nickname,
    List<RoleResponse> roles
) {

    public static RegisterResponse from(User user) {
        return new RegisterResponse(
            user.getUsername(),
            user.getNickname(),
            List.of(new RoleResponse(user.getUserRole()))
        );
    }

    public record RoleResponse(
        UserRole role
    ) {

    }
}
