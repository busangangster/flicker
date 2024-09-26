package com.flicker.bff.dto.client;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    public String nickname;
    public Double reviewRating;
    public String content;
    public boolean isSpoiler;
    public Integer likes;
    public boolean liked;
    public LocalDateTime createdAt;
}
