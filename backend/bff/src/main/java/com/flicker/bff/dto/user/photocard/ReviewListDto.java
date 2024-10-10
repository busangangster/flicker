package com.flicker.bff.dto.user.photocard;

import lombok.Data;

import java.util.List;

@Data
public class ReviewListDto {
    private List<ReviewDto> reviews;
}
