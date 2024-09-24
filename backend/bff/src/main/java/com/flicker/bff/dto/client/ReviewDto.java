package com.flicker.bff.dto.client;

import lombok.Data;
@Data
public class ReviewDto {
    public String nickname;
    public Double reviewRating;
    public String content;
    public boolean isSpoiler;
    public Integer likeCount;
    public boolean likedReview;
}
