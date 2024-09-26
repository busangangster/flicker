package com.flicker.user.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class UnlikeMovie {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unlikeMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void updateUser(User user) {
        this.user = user;
    }

    public void deleteBookmarkMovie(){
        this.isActive = 0;
    }

    protected UnlikeMovie() {}

    public UnlikeMovie(Long movieSeq){
        this.movieSeq = movieSeq;
        this.isActive = 1;
        this.createdAt = LocalDateTime.now();
    }
}
