package com.example.baro_intern.domain.user.repository;

import com.example.baro_intern.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(@NotBlank String username);
}
