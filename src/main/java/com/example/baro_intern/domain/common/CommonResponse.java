package com.example.baro_intern.domain.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private CommonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 데이터를 포함한 성공 응답
    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(true, message, data);
    }

    // 데이터 없이 메시지만 포함한 성공 응답
    public static <T> CommonResponse<T> success(String message) {
        return new CommonResponse<>(true, message, null);
    }
}

