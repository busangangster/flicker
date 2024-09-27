package com.flicker.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JwtAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 특정 경로 (로그인, 회원가입 등)에 대해서는 토큰 검증을 건너뜀
        if (requestURI.equals("/api/users/login") || requestURI.equals("/api/users/register")) {
            filterChain.doFilter(request, response); // 검증 없이 다음 필터로 넘김
            return;
        }


        String token = resolveToken(request);

        if (token != null) {
//            System.out.println("Token extracted: " + token);

            if (jwtUtil.validateToken(token)) {
                System.out.println("토큰 인증 성공");
                String username = jwtUtil.getUserId(token);


//                System.out.println("username = " + username);
//                System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = User.withUsername(username)
                            .password("") // 비밀번호는 검증하지 않음
                            .authorities("ROLE_USER") // 적절한 권한 추가
                            .build();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

//                    System.out.println("Authentication object set in SecurityContext for user: " + username);
                }
            } else {
                System.out.println("토큰 인증 실패");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 유효하지 않은 토큰에 대해 401 응답
                return; // 필터 체인 중단
            }
        } else {
            System.out.println("토큰이 없음");
        }

//        System.out.println("Proceeding with filter chain...");
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null) {
//            System.out.println("Authorization Header: " + bearerToken);
//        } else {
//            System.out.println("Authorization Header is missing");
//        }
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 반환
        }
        return null;
    }

}
