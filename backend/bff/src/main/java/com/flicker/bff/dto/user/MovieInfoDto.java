package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class MovieInfoDto {
    private Integer movieSeq;
    private String movieTitle;
    private Integer movieYear;
    private String moviePosterUrl;
    private String backgroundUrl;
}
