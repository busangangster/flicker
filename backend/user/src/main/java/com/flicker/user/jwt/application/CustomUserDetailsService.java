package com.flicker.user.jwt.application;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.jwt.CustomUserDetails;
import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.infrastructure.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        User user = userRepository.findByUserId(username);
        if(user == null) {
            throw new RestApiException(StatusCode.INVALID_ID_OR_PASSWORD);
        }
        // UserDetails 객체로 변환
        return new CustomUserDetails(user);
    }
}
