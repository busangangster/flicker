package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByKeywordInTitlePlotActorGenre(String keyword, String delYN);
}
