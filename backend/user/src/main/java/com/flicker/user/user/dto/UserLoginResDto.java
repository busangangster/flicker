package com.flicker.user.user.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLoginResDto {
    private Integer userSeq;
    private String userId;
    private String email;
    private String nickname;
    private LocalDate birthDate;
    private Character gender;
    private String profilePhotoUrl;
}
