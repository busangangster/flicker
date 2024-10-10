package com.flicker.bff.dto.user.photocard;

import lombok.Data;

@Data
public class MovieImageDto {
    private String moviePosterUrl;
    private String movieTitle;
    private Integer movieYear;
    private String backgroundUrl;
}
