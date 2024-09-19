package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.Actor;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.ActorRequest;
import com.flicker.movie.movie.dto.MovieRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieBuilderUtil {

    // Movie 빌더 메서드
    public Movie buildMovie(MovieDetail movieDetail) {
        return Movie.builder()
                .movieDetail(movieDetail)
                .build();
    }

    // MovieDetail 빌더 메서드 (MovieRequest 인터페이스 사용)
    public MovieDetail buildMovieDetail(MovieRequest request) {
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
    public List<Actor> buildActorList(ActorRequest request) {
        return request.getActorList().stream()
                .map(actorRequest -> Actor.builder()
                        .actorName(actorRequest.getActorName()) // 배우 이름 설정
                        .role(actorRequest.getRole()) // 역할 설정
                        .build())
                .collect(Collectors.toList());
    }

}
