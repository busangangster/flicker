package com.flicker.user.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    public String email;
    public String password;
    public String nickname;
}
