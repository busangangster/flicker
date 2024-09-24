package com.flicker.bff.dto;

import lombok.Data;

@Data
public class MovieResponse {
    private int movieSeq;
    private String movieTitle;
    private String moviePosterUrl;
}
