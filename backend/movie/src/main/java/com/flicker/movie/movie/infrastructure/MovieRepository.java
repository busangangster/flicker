package com.flicker.movie.movie.infrastructure;

import com.flicker.movie.movie.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // MovieDetail에 접근하여 movieTitle과 movieYear를 기준으로 중복 여부를 확인
    Optional<Movie> findByMovieDetail_MovieTitleAndMovieDetail_MovieYear(String movieTitle, int movieYear);
}
