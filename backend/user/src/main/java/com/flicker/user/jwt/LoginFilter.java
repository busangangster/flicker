package com.flicker.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.dto.UserLoginReqDto;
import com.flicker.user.user.dto.UserLoginResDto;
import com.flicker.user.user.infrastructure.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

//        System.out.println("로그인 시도");


        UserLoginReqDto loginDto = new UserLoginReqDto();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ServletInputStream inputStream = req.getInputStream();
            String messagebody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = mapper.readValue(messagebody, UserLoginReqDto.class);
        } catch (IOException e) {
            // TODO : 예외 처리
            throw new RuntimeException(e);
        }

        String username = loginDto.getUserId();
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

        System.out.println("로그인 성공");

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        if (iterator.hasNext()) {
            GrantedAuthority authority = iterator.next();
            String role = authority.getAuthority();

            // @TODO : DB에서 데이터 들고오고 createToken에 넣기

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

            UserLoginResDto dto = UserLoginResDto.builder()
                    .userSeq(userDetails.getUserSeq())
                    .userId(userDetails.getUserId())
                    .email(userDetails.getEmail())
                    .nickname(userDetails.getNickname())
                    .birthDate(userDetails.getBirthDate())
                    .gender(userDetails.getGender())
                    .profilePhotoUrl(userDetails.getProfilePhotoUrl())
                    .build();

            String access = jwtUtil.createToken("access", dto, role, 600000L);
            String refresh = jwtUtil.createToken("refresh", dto, role, 86400000L);

            // TODO : Refresh Token 저장 로직 구현
            System.out.println("액세스 토큰 발급 : "+ access);
            System.out.println("리프레시 토큰 발급 :"+refresh);

            response.addHeader("Authorization", "Bearer " + access);
            response.addCookie(createCookie("refresh", refresh));


            // 응답 본문에 간단하게 "OK" 메시지 전송
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("OK");
            response.getWriter().flush();
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed.");
            response.getWriter().flush();
        }
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(400);
        // 실패 이유 확인
        String errorMessage = "아이디와 비밀번호가 틀렸습니다.";
        // 에러 메시지 전송
        response.getWriter().write(errorMessage);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

}
