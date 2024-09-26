package com.flicker.bff.dto;

import lombok.Data;

@Data
public class MovieListResponse {
    private int movieSeq;
    private String movieTitle;
    private String moviePosterUrl;
}
