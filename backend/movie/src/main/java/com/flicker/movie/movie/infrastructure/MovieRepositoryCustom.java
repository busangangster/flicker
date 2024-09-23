package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MovieRepositoryCustom {
    List<Movie> findByKeywordInTitlePlotActorGenre(String keyword, String delYN, Pageable pageable);
}
