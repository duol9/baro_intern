package com.example.baro_intern.domain.common.exception;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(String code, String message) {
        return new ExceptionResponse(code, message);
    }
}
