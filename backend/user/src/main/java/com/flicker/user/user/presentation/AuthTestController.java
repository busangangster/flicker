package com.flicker.user.user.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.application.UserService;
import com.flicker.user.user.domain.UserConverter;
import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.infrastructure.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth-test")
@RequiredArgsConstructor
public class AuthTestController {

    private final UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<ResponseDto> login(HttpServletRequest request){

        Cookie[] cookies = request.getCookies(); // 요청에서 쿠키 배열 가져오기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
            }
        }


        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            throw new RestApiException(StatusCode.INVALID_ID_OR_PASSWORD);
        }

        User byUserId = userRepository.findByUserId(userId);

        return ResponseDto.response(StatusCode.SUCCESS ,byUserId);
    }


}
