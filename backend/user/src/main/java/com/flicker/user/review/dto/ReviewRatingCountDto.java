package com.flicker.user.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@ToString
public class ReviewRatingCountDto {
    public Double reviewRating; // 평점
    public Long count;          // 평점별 리뷰 개수

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewRatingCountDto that = (ReviewRatingCountDto) o;
        return Objects.equals(reviewRating, that.reviewRating);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reviewRating);
    }
}
