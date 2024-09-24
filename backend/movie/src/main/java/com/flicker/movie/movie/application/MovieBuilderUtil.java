package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.*;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.KeywordCount;
import com.flicker.movie.movie.dto.MovieInfoEvent;
import com.flicker.movie.movie.dto.ActorRequest;
import com.flicker.movie.movie.dto.MovieRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    // WordCloud 리스트 빌더 메서드
    public List<WordCloud> buildWordCloudList(List<KeywordCount> keywordCounts) {
        return keywordCounts.stream()
                .map(keywordCount -> WordCloud.builder()
                        .keyword(keywordCount.getKeyword()) // 키워드 설정
                        .count(keywordCount.getCount()) // 키워드 빈도 설정
                        .build())
                .collect(Collectors.toList());
    }

    // MongoMovieList 빌더 메서드
    public MongoMovieList buildMongoMovieList(List<Movie> movieList) {
        // movieList를 MongoMovieList로 변환
        List<MongoMovie> mongoMovies = movieList.stream()
                .map(movie -> MongoMovie.builder()
                        .movieSeq(movie.getMovieSeq()) // 영화 ID 설정
                        .movieTitle(movie.getMovieDetail().getMovieTitle()) // 영화 제목 설정
                        .moviePosterUrl(movie.getMovieDetail().getMoviePosterUrl()) // 영화 포스터 URL 설정
                        .build())
                .collect(Collectors.toList());
        // MongoMovieList 생성
        return MongoMovieList.builder()
                .mongoMovies(mongoMovies)
                .build();
    }

    // SearchResult 빌더 메서드
    public static SearchResult buildSearchResult(String keyword, String mongoKey) {
        return SearchResult.builder()
                .keyword(keyword)
                .mongoKey(mongoKey)
                .expiration(86400L) // 24시간
                .build();
    }

    // MongoUserAction 빌더 메서드
    public MongoUserAction buildMongoUserAction(int userSeq, Integer movieSeq, String keyword, String action, LocalDateTime timestamp) {
        return MongoUserAction.builder()
                .userSeq(userSeq)
                .movieSeq(movieSeq)
                .keyword(keyword)
                .action(action)
                .timestamp(timestamp)
                .build();
    }


    // MovieInfoEvent 빌더 메서드
    public MovieInfoEvent buildMovieInfoEvent(int movieSeq, String type, String action, LocalDateTime timestamp) {
        return MovieInfoEvent.builder()
                .movieSeq(movieSeq)
                .type(type)
                .action(action)
                .timestamp(timestamp)
                .build();
    }
}
