package com.flicker.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.user.user.dto.UserLoginResDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isTokenExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            System.out.println("Token validation successful");
            return true;
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String createToken(String category, UserLoginResDto dto, String role, long exp) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // LocalDate를 문자열로 변환할 포맷터

        System.out.println("dto 를 변환 = " + dto);

        String token = Jwts.builder()
                .claim("category", category)
                .claim("userId", dto.getUserId())
                .claim("email", dto.getEmail())
                .claim("nickname", dto.getNickname())
                .claim("birthDate", dto.getBirthDate().format(formatter))  // LocalDate -> String
                .claim("gender", dto.getGender())
                .claim("profilePhotoUrl", dto.getProfilePhotoUrl())
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(secretKey)
                .compact();

        System.out.println("JWT 토큰이 생성 됨 : " + token);

        return token;
    }
}
