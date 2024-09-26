package com.flicker.user.review.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeReviewSeq;

    private Integer reviewSeq;
    private Integer userSeq;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_seq")
    private Review review;
}
