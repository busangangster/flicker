package com.flicker.bff.dto.user.photocard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoCardDto {
    private ReviewDto reviewDto;
    private MovieImageDto movieImageDto;
}
