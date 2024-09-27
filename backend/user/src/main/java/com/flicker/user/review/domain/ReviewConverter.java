package com.flicker.user.review.domain;

import com.flicker.user.review.domain.entity.LikeReview;
import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.ReviewDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewConverter {

    public ReviewDto reviewToReviewDto(Review review, String nickname, Integer myUserSeq) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewSeq(review.getReviewSeq());
        dto.setUserSeq(review.getUserSeq());
        dto.setMovieSeq(review.getMovieSeq());
        dto.setReviewRating(review.getReviewRating());
        dto.setContent(review.getContent());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setSpoiler(review.getIsSpoiler());
        dto.setNickname(nickname);
        dto.setLikes(review.getLikes());

        dto.setLiked(false);

//        System.out.println("라이크리뷰스조회");
        for(LikeReview likeReview : review.getLikeReviews()){
//            System.out.println("liked 찾기");
            if(likeReview.getUserSeq().equals(myUserSeq)){
//                System.out.println("찾기 성공");
                dto.setLiked(true);
                break;
            }
        }

        return dto;
    }

}
