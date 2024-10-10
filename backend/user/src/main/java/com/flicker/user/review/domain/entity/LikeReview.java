package com.flicker.user.review.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeReviewSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_seq")
    private Review review;
    private Integer userSeq;

    private LocalDateTime createdAt;



    protected LikeReview() {
    }

    public LikeReview(Integer userSeq) {
        this.userSeq = userSeq;
        this.createdAt = LocalDateTime.now();
    }

    public void updateReview(Review review) {
        this.review = review;
    }
}
