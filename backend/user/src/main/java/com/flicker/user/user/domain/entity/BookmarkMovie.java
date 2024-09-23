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
public class BookmarkMovie {

    @Id
    private Long bookmarkMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void deleteBookmarkMovie(){
        this.isActive = 0;
    }

    protected BookmarkMovie() {}

    public BookmarkMovie(Long bookmarkMovieSeq) {
        this.bookmarkMovieSeq = bookmarkMovieSeq;
        this.isActive = 1;
        this.createdAt = LocalDateTime.now();
    }
}
