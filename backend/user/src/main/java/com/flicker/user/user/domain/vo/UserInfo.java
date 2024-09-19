package com.flicker.user.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private LocalDate birthDate;
    private Character gender;

}
