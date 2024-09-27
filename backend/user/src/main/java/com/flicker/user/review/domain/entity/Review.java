package com.flicker.user.review.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Boolean isSpoiler;
    private Integer isActive;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeReview> likeReviews;

    private Integer likes;

    public boolean removeLikeReview(Integer userSeq){
        for(LikeReview likeReview : likeReviews){
            if(likeReview.getUserSeq().equals(userSeq)){
                likeReviews.remove(likeReview);
                return true;
            }
        }
        return false;
    }

    public boolean addLikeReview(Integer userSeq){

        for(LikeReview likeReview : likeReviews){
            if(likeReview.getUserSeq().equals(userSeq)){
                return false;
            }
        }

        LikeReview likeReview = new LikeReview(userSeq);
        likeReview.updateReview(this);
        likeReviews.add(likeReview);

        return true;
    }

    protected Review() {}

    public Review(String content, Boolean isSpoiler, Integer movieSeq, Double reviewRating, Integer userSeq) {
        this.content = content;
        this.isSpoiler = isSpoiler;
        this.movieSeq = movieSeq;
        this.reviewRating = reviewRating;
        this.userSeq = userSeq;

        this.likeReviews = new ArrayList<>();
        this.likes = 0;
        this.createdAt = LocalDateTime.now();
        this.isActive = 1;
    }
}
