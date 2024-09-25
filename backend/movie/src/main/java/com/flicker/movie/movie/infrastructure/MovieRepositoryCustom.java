package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MovieRepositoryCustom {

    // 영화 제목, 줄거리, 배우, 장르를 기준으로 키워드를 포함하는 영화 목록 조회
    List<Movie> findByKeywordInTitlePlotActorGenre(String keyword, String delYN, Pageable pageable);
}
