package com.flicker.user.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDto {
    private String userId;
    private String email;
    private String password;
    private String passCheck;
    private String nickname;
    private LocalDate birthDate;
    private Character gender;
}
