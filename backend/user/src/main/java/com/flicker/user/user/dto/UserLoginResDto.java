package com.flicker.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResDto {
    private String userId;
    private String email;
    private String nickname;
    private LocalDate birthDate;
    private Character gender;
}
