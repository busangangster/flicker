package com.flicker.bff.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private int userSeq;
    private String userId;
    private String nickName;
    private String profilePhotoUrl;
    private int movieSeq;
    private double reviewRating;
    private String content;
    private LocalDateTime createdAt;
    private boolean isSpoiler;
}
