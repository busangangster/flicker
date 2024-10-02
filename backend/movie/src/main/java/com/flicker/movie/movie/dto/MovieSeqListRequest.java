package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieSeqListRequest {
    private String movieTitle;
    private int movieYear;
}
