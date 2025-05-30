package com.example.baro_intern.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

    @NotBlank
    String username,

    @NotBlank
    String password,

    @NotBlank
    String nickname
) {

}
