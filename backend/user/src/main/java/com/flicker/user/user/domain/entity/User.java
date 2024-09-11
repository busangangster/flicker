package com.flicker.user.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberSeq;

    @Column(nullable = false)
    private String userName;

    private String email;
    private String hashedPass;
    private String fullName;
    private String nickname;
    private LocalDate birthDate;
    private Integer age;
    private String gender;
    private DateTime
}
