package com.flicker.movie.movie.domain.vo;

import com.flicker.common.module.exception.RestApiException;
import com.flicker.common.module.status.StatusCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@ToString
@AllArgsConstructor
public class MovieDetail {

    @Column(nullable = false)
    private final String movieTitle; // 영화 제목

    @Column
    private final String director; // 감독 이름

    @Column
    private final String genre; // 영화 장르

    @Column
    private final String country; // 제작 국가

    @Column(length = 5000)
    private final String moviePlot; // 영화 줄거리

    @Column(length = 50)
    private final String audienceRating; // 관람 등급

    @Column(nullable = false)
    private final int movieYear; // 영화 제작 연도

    @Column(length = 50)
    private final String runningTime; // 영화 상영 시간

    @Column(length = 500)
    private final String moviePosterUrl; // 영화 포스터 이미지 URL

    @Column(length = 500)
    private final String trailerUrl; // 영화 예고편 URL

    @Column(length = 500)
    private final String backgroundUrl; // 영화 배경 이미지 URL

    // JPA에서 사용되는 기본 생성자 (필수, 외부에서 직접 호출되지 않음)
    public MovieDetail() {
        this.movieTitle = null;
        this.director = null;
        this.genre = null;
        this.country = null;
        this.moviePlot = null;
        this.audienceRating = null;
        this.movieYear = 0;
        this.runningTime = null;
        this.moviePosterUrl = null;
        this.trailerUrl = null;
        this.backgroundUrl = null;
    }

    // 빌더 내부에서 유효성 검증 추가 (길이 검증 포함)
    public static class MovieDetailBuilder {
        public MovieDetail build() {
            validate();  // 빌드 시 유효성 검증 수행
            return new MovieDetail(
                    movieTitle, director, genre, country, moviePlot, audienceRating, movieYear,
                    runningTime, moviePosterUrl, trailerUrl, backgroundUrl
            );
        }

        // 유효성 검증 메서드 (길이 검증 포함)
        private void validate() {
            if (movieTitle == null || movieTitle.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 제목을 확인해주세요 (길이초과 또는 null).");
            } else if (movieYear < 1888 || movieYear > LocalDate.now().getYear()) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 제작 연도가 범위를 벗어났습니다.");
            } else if (director != null && director.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "감독 이름을 확인해주세요 (길이초과).");
            } else if (genre != null && genre.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 장르를 확인해주세요 (길이초과).");
            } else if (country != null && country.length() > 255) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "제작 국가를 확인해주세요 (길이초과).");
            } else if (moviePlot != null && moviePlot.length() > 5000) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 줄거리를 확인해주세요 (길이초과).");
            } else if (audienceRating != null && audienceRating.length() > 50) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "관람 등급을 확인해주세요 (길이초과).");
            } else if (runningTime != null && runningTime.length() > 50) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "상영 시간을 확인해주세요 (길이초과).");
            } else if (moviePosterUrl != null && moviePosterUrl.length() > 500) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 포스터 URL을 확인해주세요 (길이초과).");
            } else if (trailerUrl != null && trailerUrl.length() > 500) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 예고편 URL을 확인해주세요 (길이초과).");
            } else if (backgroundUrl != null && backgroundUrl.length() > 500) {
                throw new RestApiException(StatusCode.BAD_REQUEST, "영화 배경 이미지 URL을 확인해주세요 (길이초과).");
            }
        }

    }
}
