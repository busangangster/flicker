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

    @Id
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

    public FavoriteMovie(Long favoriteMovieSeq) {
        this.favoriteMovieSeq = favoriteMovieSeq;
        this.createdAt = LocalDateTime.now();
        this.isActive = 1;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteMovie that = (FavoriteMovie) o;
        return Objects.equals(favoriteMovieSeq, that.favoriteMovieSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(favoriteMovieSeq);
    }
}
