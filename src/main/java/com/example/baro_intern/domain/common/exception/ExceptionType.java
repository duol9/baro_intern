package com.example.baro_intern.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {

    // Auth
    JWT_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "JWT_TOKEN_REQUIRED", "JWT 토큰이 필요합니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "ACCESS_DENIED", "접근 권한이 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다."),

    //User
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "INVALID_USER_ROLE", "유효하지 않은 UserRole"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS", "이미 가입된 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_ADMIN(HttpStatus.BAD_REQUEST, "USER_ALREADY_ADMIN", "이미 관리자 권한을 보유하고 있습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
