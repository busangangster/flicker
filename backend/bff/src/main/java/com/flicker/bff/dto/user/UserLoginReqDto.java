package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class UserLoginReqDto {
    private String userId;
    private String password;
}
