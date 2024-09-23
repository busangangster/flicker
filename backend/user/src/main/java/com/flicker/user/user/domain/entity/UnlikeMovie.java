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

    @Id
    private Long unlikeMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void deleteBookmarkMovie(){
        this.isActive = 0;
    }

    protected UnlikeMovie() {}

    public UnlikeMovie(Long unlikeMovieSeq){
        this.unlikeMovieSeq = unlikeMovieSeq;
        this.isActive = 1;
        this.createdAt = LocalDateTime.now();
    }
}
