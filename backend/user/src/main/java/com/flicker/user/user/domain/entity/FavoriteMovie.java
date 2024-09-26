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
    private Long favoriteMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void deleteFavoriteMovie() {
        this.isActive = 0;
    }

    protected FavoriteMovie() {}

    public FavoriteMovie(Long movieSeq) {
        this.movieSeq = movieSeq;
        this.createdAt = LocalDateTime.now();
        this.isActive = 1;
    }

    public void updateUser(User user) {
        this.user = user;
    }

}
