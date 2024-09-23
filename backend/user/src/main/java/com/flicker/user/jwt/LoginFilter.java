package com.flicker.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.user.user.dto.UserLoginReqDto;
import com.flicker.user.user.infrastructure.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
//    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

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

        String username = auth.getName();

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        if (iterator.hasNext()) {
            GrantedAuthority authority = iterator.next();
            String role = authority.getAuthority();

            // @TODO : DB에서 데이터 들고오고 createToken에 넣기

            String access = jwtUtil.createToken("access", username, role, 600000L);
            String refresh = jwtUtil.createToken("refresh", username, role, 86400000L);


            // TODO : Refresh Token 저장 로직 구현

            response.addHeader("Authorization", "Bearer " + access);
            response.addCookie(createCookie("refresh", refresh));
        }
        else{
            // TODO 예외 처리
        }
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // TODO : 응답 에러코드와 함께 메세지 함께 보내야함.
        response.setStatus(401);
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
        setFilterProcessesUrl("/api/users/login");
    }

}
