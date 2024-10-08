package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import io.lettuce.core.api.push.PushMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>, MovieRepositoryCustom {

    // 영화 제목과 영화 연도, 그리고 DEL_YN이 'N'인 영화 조회
    Optional<Movie> findByMovieDetail_MovieTitleAndMovieDetail_MovieYearAndDelYN(String movieTitle, int movieYear, String delYN);

    // DEL_YN이 N인 영화 전체 목록 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByDelYNOrderByMovieDetail_MovieYearDescMovieSeqAsc(String delYN, Pageable pageable);

    // DEL_YN이 N이고, 영화 장르별 영화 목록 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByMovieDetail_GenreContainingAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqAsc(String genre, String delYN, Pageable pageable);

    // DEL_YN이 N이고, 해당 배우가 출연한 영화 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByActors_ActorNameAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(String actorName, String delYN, Pageable pageable);

    // DEL_YN이 N이고, 국가별 영화 조회 (영화 제작 연도 내림차순)
    Page<Movie> findByMovieDetail_CountryContainingAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqAsc(String country, String n, Pageable pageable);

    // DEL_YN이 N이고, 연도별 영화 조회
    Page<Movie> findByMovieDetail_MovieYearAndDelYNOrderByMovieSeqAsc(int year, String delYN, Pageable pageable);

    // DEL_YN이 N이고, 영화 고유 식별자 목록을 기준으로 영화 목록 조회
    List<Movie> findByMovieSeqInAndDelYN(List<Integer> movieSeqList, String delYN);

    // 영화 제목이 같은 것 중에서 DEL_YN이 'N'이고, 영화 연도가 최신인 영화 한 건만 조회
    Optional<Movie> findFirstByMovieDetail_MovieTitleAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(String movieTitle, String delYN);

    // DEL_YN이 N이고, 영화 평점 내림차순으로 영화 목록 조회
    List<Movie> findByDelYNOrderByMovieRatingDesc(String n, Pageable pageable);
}
