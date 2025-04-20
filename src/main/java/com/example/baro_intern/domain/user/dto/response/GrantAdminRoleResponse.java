package com.example.baro_intern.domain.user.dto.response;

import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.entity.UserRole;
import java.util.List;

public record GrantAdminRoleResponse(
    String username,
    String nickname,
    List<RoleResponse> roles
) {

    public static GrantAdminRoleResponse from(User user) {
        return new GrantAdminRoleResponse(
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
