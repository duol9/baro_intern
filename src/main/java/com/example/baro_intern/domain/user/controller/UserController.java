package com.example.baro_intern.domain.user.controller;

import com.example.baro_intern.domain.common.CommonResponse;
import com.example.baro_intern.domain.user.dto.response.GrantAdminRoleResponse;
import com.example.baro_intern.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 권한 부여 API", description = "관리자 권한 부여 로직을 수행하기 위한 Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "관리자 권한 부여", security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "권한 부여 성공",
                content = @Content(schema = @Schema(implementation = GrantAdminRoleResponse.class)))
        }
    )
    public ResponseEntity<CommonResponse<GrantAdminRoleResponse>> grantAdminRole (
        @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success("권한 부여 성공", userService.grantAdminRole(userId)));
    }
}
