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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkMovieSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
    private Integer movieSeq;
    private LocalDateTime createdAt;
    private Integer isActive;

    public void updateUser(User user) {
        this.user = user;
    }

    public void deleteBookmarkMovie(){
        this.isActive = 0;
    }

    protected BookmarkMovie() {}

    public BookmarkMovie(Integer movieSeq) {
        this.movieSeq = movieSeq;
        this.isActive = 1;
        this.createdAt = LocalDateTime.now();
    }
}
