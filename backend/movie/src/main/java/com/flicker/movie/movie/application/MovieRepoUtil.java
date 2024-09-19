package com.flicker.movie.movie.application;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.infrastructure.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * MovieRepoUtil은 MovieRepository를 감싸서 영화 정보를 조회하고 저장하는
 * 공통 기능을 제공하는 유틸리티 클래스입니다.
 *
 * 이 클래스는 주로 영화 정보에 대한 데이터베이스 조회 및 저장 작업을 처리하며,
 * 예외 처리를 포함하여 공통된 로직을 재사용할 수 있도록 설계되었습니다.
 */
@RequiredArgsConstructor
@Component
public class MovieRepoUtil {

    private final MovieRepository movieRepository;

    /**
     * 영화 ID(movieSeq)를 사용하여 영화 정보를 조회하는 메서드입니다.
     *
     * @param movieSeq 조회할 영화의 ID
     * @return 조회된 영화 객체
     * @throws RestApiException 영화 정보를 찾을 수 없을 때 발생
     */
    public Movie findById(int movieSeq) {
        return movieRepository.findById(movieSeq)
                .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, "해당 영화 정보를 찾을 수 없습니다."));
    }

    /**
     * 영화 정보를 데이터베이스에 저장하는 메서드입니다.
     *
     * @param movie 저장할 영화 객체
     * @throws RestApiException 영화 정보 저장 중 오류가 발생할 경우 발생
     */
    public void saveMovie(Movie movie) {
        try {
            movieRepository.save(movie);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 정보 저장 중 오류가 발생했습니다.");
        }
    }

    /**
     * 영화 제목과 제작 연도를 기준으로 중복된 영화가 있는지 확인하는 메서드입니다.
     *
     * @param movieTitle 중복 확인할 영화의 제목
     * @param movieYear  중복 확인할 영화의 제작 연도
     * @throws RestApiException 중복된 영화가 존재할 경우 발생
     */
    public void isDuplicatedMovie(String movieTitle, int movieYear) {
        Optional<Movie> movie = movieRepository.findByMovieDetail_MovieTitleAndMovieDetail_MovieYear(movieTitle, movieYear);
        if (movie.isPresent()) {
            throw new RestApiException(StatusCode.DUPLICATE_MOVIE, "중복된 영화 정보가 존재합니다.");
        }
    }
}
