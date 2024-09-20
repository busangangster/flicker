package com.flicker.user.user.application;

import com.flicker.user.user.dto.UserLoginReqDto;
import com.flicker.user.user.dto.UserLoginResDto;
import com.flicker.user.user.dto.UserRegisterDto;

public interface UserService {

    public boolean register(UserRegisterDto dto);
    public UserLoginResDto login(UserLoginReqDto dto);
}
