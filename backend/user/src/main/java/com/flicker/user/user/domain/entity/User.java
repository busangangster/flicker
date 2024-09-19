package com.flicker.user.user.domain.entity;

import com.flicker.user.user.domain.UserGrade;
import com.flicker.user.user.domain.vo.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.NoSuchElementException;

@Entity
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false)
    private String userName;
    private String email;
    private String hashedPass;
    private String nickname;
    private LocalDate birthDate;
    private Character gender;
    private String profilePhotoUrl;

    @Embedded
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

    protected User() {
    }

    public User(LocalDate birthDate, String email, Character gender, String hashedPass, String nickname, String profilePhotoUrl, String userName) {
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.hashedPass = hashedPass;
        this.nickname = nickname;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userName = userName;

        this.role = new Role(UserGrade.USER);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = 1;
    }

    public Integer getAge(){
        if(this.birthDate == null){
            throw new NoSuchElementException();
        }
        LocalDate now = LocalDate.now();
        return Period.between(this.birthDate, now).getYears();
    }
}
