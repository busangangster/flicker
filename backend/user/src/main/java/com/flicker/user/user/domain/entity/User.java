package com.flicker.user.user.domain.entity;

import com.flicker.user.user.domain.vo.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    @Column(nullable = false)
    private String userName;
    private String email;
    private String hashedPass;
//    private String fullName;
    private String nickname;
    private LocalDate birthDate;
//    private Integer age;
    private Character gender;
    private String profilePhotoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isActive;

    /*
        role을 반드시 가져야 한다.
     */
    @Embedded
    private Role role;
}
