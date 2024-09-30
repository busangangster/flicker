package com.flicker.bff.dto.user.photocard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Integer movieSeq;
    private Integer reviewSeq;
    private String nickname;
    private Double reviewRating;
    private String content;
    private Boolean spoiler;
    private Integer likes;
    private Boolean liked;
    private LocalDateTime createdAt;
    private Boolean top;

}
