package com.flicker.user.user.dto;

import lombok.Data;

@Data
public class UserLoginReqDto {
    private String userId;
    private String password;
}
