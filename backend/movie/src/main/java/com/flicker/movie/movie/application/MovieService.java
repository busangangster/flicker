package com.flicker.movie.movie.application;

import com.flicker.common.module.exception.RestApiException;
import com.flicker.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.entity.Actor;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.*;
import com.flicker.movie.movie.infrastructure.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // MovieDetail 빌더 메서드 (MovieRequest 인터페이스 사용)
    private MovieDetail buildMovieDetail(MovieRequest request) {
        return MovieDetail.builder()
                .movieTitle(request.getMovieTitle()) // 영화 제목 설정
                .director(request.getDirector()) // 감독 설정
                .movieYear(request.getMovieYear()) // 영화 제작 연도 설정
                .moviePlot(request.getMoviePlot()) // 영화 줄거리 설정
                .moviePosterUrl(request.getMoviePosterUrl()) // 영화 포스터 URL 설정
                .genre(request.getGenre()) // 장르 설정
                .backgroundUrl(request.getBackgroundUrl()) // 배경 이미지 URL 설정
                .country(request.getCountry()) // 제작 국가 설정
                .runningTime(request.getRunningTime()) // 상영 시간 설정
                .audienceRating(request.getAudienceRating()) // 관람 등급 설정
                .trailerUrl(request.getTrailerUrl()) // 예고편 URL 설정
                .build();
    }

    // Actor 리스트 빌더 메서드 (ActorRequest 인터페이스 사용)
    private List<Actor> buildActorList(ActorRequest request) {
        return request.getActorList().stream()
                .map(actorRequest -> Actor.builder()
                        .actorName(actorRequest.getActorName()) // 배우 이름 설정
                        .role(actorRequest.getRole()) // 역할 설정
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createMovie(MovieCreateRequest request) {
        // MovieDetail 빌드
        MovieDetail movieDetail = buildMovieDetail(request);
        // Movie 엔티티 생성
        Movie movie = Movie.builder()
                .movieDetail(movieDetail)
                .build();
        // Actor 리스트 생성
        List<Actor> actorList = buildActorList(request);
        // Movie Actor 리스트 추가
        movie.addActors(actorList);
        // 데이터베이스에 저장
        movieRepository.save(movie);
    }

    @Transactional
    public void updateMovie(MovieUpdateRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepository.findById(request.getMovieSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, "해당 영화 정보를 찾을 수 없습니다."));
        // 2. 영화 정보 업데이트
        MovieDetail movieDetail = buildMovieDetail(request); // MovieDetail 빌드
        movie.updateMovieDetail(movieDetail);
    }

    @Transactional
    public void updateMovieRating(MovieRatingUpdateRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepository.findById(request.getMovieSeq())
                .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, "해당 영화 정보를 찾을 수 없습니다."));
        // 2. 영화 평점 업데이트
        movie.updateMovieRating(request.getMovieRating());
    }
}
