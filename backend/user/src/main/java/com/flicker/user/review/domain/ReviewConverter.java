package com.flicker.user.review.domain;

import com.flicker.user.review.domain.entity.Review;
import com.flicker.user.review.dto.ReviewDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewConverter {

    public ReviewDto reviewToReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewSeq(review.getReviewSeq());
        dto.setUserSeq(review.getUserSeq());
        dto.setMovieSeq(review.getMovieSeq());
        dto.setReviewRating(review.getReviewRating());
        dto.setContent(review.getContent());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setIsSpoiler(review.getIsSpoiler());
        return dto;
    }

    public List<ReviewDto> reviewListToReviewDtoList(List<Review> reviewList) {
        List<ReviewDto> dtoList = new ArrayList<ReviewDto>();
        for (Review review : reviewList) {
            dtoList.add(reviewToReviewDto(review));
        }
        return dtoList;
    }

}
