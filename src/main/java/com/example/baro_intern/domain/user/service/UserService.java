package com.example.baro_intern.domain.user.service;

import com.example.baro_intern.domain.common.exception.CustomException;
import com.example.baro_intern.domain.common.exception.ExceptionType;
import com.example.baro_intern.domain.user.dto.response.GrantAdminRoleResponse;
import com.example.baro_intern.domain.user.entity.User;
import com.example.baro_intern.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public GrantAdminRoleResponse grantAdminRole(Long userId) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        findUser.updateAdminRole();

        return GrantAdminRoleResponse.from(findUser);
    }

}
