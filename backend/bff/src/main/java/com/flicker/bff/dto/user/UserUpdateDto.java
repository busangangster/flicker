package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class UserUpdateDto {
    public String email;
    public String password;
    public String nickname;
}
