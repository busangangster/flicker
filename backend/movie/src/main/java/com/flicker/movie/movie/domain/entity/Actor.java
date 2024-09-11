package com.flicker.movie.movie.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 제한
@AllArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actorSeq;

    @Column(nullable = false)
    private String actorName;

    @Column
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_seq", nullable = false)
    private Movie movie;

    // 양방향 관계 설정을 위한 메서드
    protected void setMovie(Movie movie) {
        this.movie = movie;
    }

    // 역할 변경 메서드 (비즈니스 로직)
    public void changeRole(String newRole) {
        this.role = newRole;
    }
}
