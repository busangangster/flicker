package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteMovieListResponse {
    List<Integer> likeMovieSeqs;
}
