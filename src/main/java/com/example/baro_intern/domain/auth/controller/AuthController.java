package com.example.baro_intern.domain.auth.controller;

import com.example.baro_intern.domain.auth.dto.request.LoginRequest;
import com.example.baro_intern.domain.auth.dto.request.RegisterRequest;
import com.example.baro_intern.domain.auth.dto.response.LoginResponse;
import com.example.baro_intern.domain.auth.dto.response.RegisterResponse;
import com.example.baro_intern.domain.auth.service.AuthService;
import com.example.baro_intern.domain.user.entity.UserRole;
import com.example.baro_intern.domain.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 인증 API", description = "회원가입, 로그인과 같은 인증과 관련된 로직을 수행하기 위한 Controller")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(
        summary = "일반 사용자 회원가입",
        responses = {
            @ApiResponse(
                responseCode = "201", description = "회원가입 성공",
                content = @Content(schema = @Schema(implementation = RegisterResponse.class)))
        }
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> register (
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success("회원가입 성공", authService.register(registerRequest, UserRole.USER)));
    }

    @PostMapping("/admin-signup")
    @Operation(
        summary = "관리자 회원가입",
        responses = {
            @ApiResponse(
                responseCode = "201", description = "회원가입 성공",
                content = @Content(schema = @Schema(implementation = RegisterResponse.class)))
        }
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> adminRegister(
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success("회원가입 성공", authService.register(registerRequest, UserRole.ADMIN)));
    }

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        responses = {
            @ApiResponse(
                responseCode = "200", description = "로그인 성공",
                content = @Content(schema = @Schema(implementation = LoginResponse.class)))
        }
    )
    public ResponseEntity<CommonResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success("로그인 성공", authService.login(loginRequest)));
    }
}
