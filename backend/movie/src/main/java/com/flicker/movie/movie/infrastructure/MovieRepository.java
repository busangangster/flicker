package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>, MovieRepositoryCustom {

    // MovieDetail에 접근하여 movieTitle과 movieYear를 기준으로 중복 여부를 확인
    Optional<Movie> findByMovieDetail_MovieTitleAndMovieDetail_MovieYear(String movieTitle, int movieYear);

    // DEL_YN이 N인 영화 전체 목록 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByDelYNOrderByMovieDetail_MovieYearDesc(String delYNm, Pageable pageable);

    // DEL_YN이 N이고, 영화 제목에 특정 키워드가 포함된 영화 목록 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByMovieDetail_GenreContainingAndDelYNOrderByMovieDetail_MovieYearDesc(String genre, String delYN, Pageable pageable);

    // 해당 배우가 출연한 영화 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByActors_ActorNameContainingAndDelYNOrderByMovieDetail_MovieYearDesc(String actorName, String delYN, Pageable pageable);

    List<Movie> findByMovieSeqIn(List<Integer> movieSeqList);
}
