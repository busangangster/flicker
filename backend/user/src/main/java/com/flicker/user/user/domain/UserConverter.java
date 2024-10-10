package com.flicker.user.user.domain;

import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.dto.UserLoginResDto;
import com.flicker.user.user.dto.UserRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserLoginResDto toUserLoginResDto(User user) {
        return UserLoginResDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birthDate(user.getUserInfo().getBirthDate())
                .gender(user.getUserInfo().getGender())
                .build();
    }

    public User registerDtoToUser(UserRegisterDto dto) {
        return new User(dto.getBirthDate(), dto.getEmail(), dto.getGender(), dto.getPassword(), dto.getNickname(), dto.getUserId());
    }
}
