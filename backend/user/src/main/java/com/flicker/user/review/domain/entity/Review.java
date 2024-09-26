package com.flicker.user.review.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewSeq;

    private Integer userSeq;
    private Integer movieSeq;
    private Double reviewRating;
    private String content;
    private LocalDateTime createdAt;
    private Integer isSpoiler;
    private Boolean isActive;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeReview> likeReviews;


}
