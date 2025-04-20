package com.example.baro_intern.domain.user.entity;

import com.example.baro_intern.domain.common.exception.CustomException;
import com.example.baro_intern.domain.common.exception.ExceptionType;
import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new CustomException(ExceptionType.INVALID_USER_ROLE));
    }
}
