package com.sparta.lv2.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 600 * 60 * 1000L; // 60분
    // JWT 로그
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // JWT 생성 메서드
    public String createToken(Long id, String username) {
        Date date = new Date(); // 현재 시각

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(id)) // 사용자 식별자 값
                        .claim("username", username) // username 필드(사용자 이름)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간(현재시간 + 설정한 시간)
                        .setIssuedAt(date) // 토큰 발급입(현재 시간)
                        .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘,
                        .compact(); // 토큰 완성
    }

    // JWT Bearer 짜르는 메서드
    public String jwtSubstring(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }

    // JWT 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // 위변조 여부, 만료 여부 이 한 줄로 검증 가능함
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JWT에서 사용자 정보 가져오기
    public String getUserInfo(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    public Claims getUserInfoClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token)
                .getBody();
    }

}
