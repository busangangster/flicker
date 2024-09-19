package com.flicker.user.user.domain.entity;

import com.flicker.user.user.dto.UserAndMovieIdDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class FavoriteMovie {

    @EmbeddedId
    private UserMovieId userMovieId;

    @ManyToOne
    @JoinColumn(name = "userSeq_123123")
    private User user;

    private LocalDateTime createdAt;
    private Integer isActive;

    public void deleteFavoriteMovie() {
        this.isActive = 0;
    }

    protected FavoriteMovie() {}

    public FavoriteMovie(UserAndMovieIdDto dto) {
        this.userMovieId = UserMovieId
                .builder()
                .movieSeq(dto.getMovieSeq())
                .userSeq(dto.getUserSeq())
                .build();
        this.createdAt = LocalDateTime.now();
        this.isActive = 1;
    }
}
