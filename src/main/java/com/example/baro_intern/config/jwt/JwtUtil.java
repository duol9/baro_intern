package com.example.baro_intern.config.jwt;

import com.example.baro_intern.domain.common.exception.CustomException;
import com.example.baro_intern.domain.common.exception.ExceptionType;
import com.example.baro_intern.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        String secretKey = jwtProperties.secret().key();
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long userId, String username, String nickname, UserRole userRole) {
        Date date = new Date();
        long expiration = jwtProperties.token().expiration();

        return jwtProperties.token().prefix() +
            Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + expiration))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(
            jwtProperties.token().prefix())) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ExceptionType.JWT_TOKEN_REQUIRED);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
