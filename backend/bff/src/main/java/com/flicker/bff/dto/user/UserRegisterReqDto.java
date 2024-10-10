package com.flicker.bff.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterReqDto {
    private String userId;
    private String email;
    private String password;
    private String passCheck;
    private String nickname;
    private LocalDate birthDate;
    private Character gender;
}

