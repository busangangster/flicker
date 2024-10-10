package com.flicker.user.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class FavoriteMovie {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
    private Integer movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void deleteFavoriteMovie() {
        this.isActive = 0;
    }

    protected FavoriteMovie() {}

    public FavoriteMovie(Integer movieSeq) {
        this.movieSeq = movieSeq;
        this.createdAt = LocalDateTime.now();
        this.isActive = 1;
    }

    public void updateUser(User user) {
        this.user = user;
    }

}
