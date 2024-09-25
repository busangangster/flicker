package com.flicker.user.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.and())
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .formLogin(formLogin -> formLogin.disable()) // Form 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll() // 특정 경로는 인증 없이 접근 허용
                        .requestMatchers("/api/auth-test").hasRole("USER") // auth-test 경로는 인증 필요
                        .anyRequest().authenticated() // 나머지 경로는 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
                )
                .userDetailsService(userDetailsService) // UserDetailsService 설정
                .addFilterAt(new LoginFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가



        return http.build();
    }

}
