package com.flicker.movie.movie.application;

import com.flicker.movie.movie.domain.entity.*;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.UserActionEvent;
import com.flicker.movie.movie.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieBuilderUtil movieBuilderUtil;
    private final MovieRepoUtil movieRepoUtil;
    private final CustomProducer customProducer;  // Kafka 프로듀서 주입

    @Transactional
    public void createMovie(MovieCreateRequest request) {
        // 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // MovieDetail 빌드
        MovieDetail movieDetail = movieBuilderUtil.buildMovieDetail(request);
        // Movie 엔티티 생성
        Movie movie = movieBuilderUtil.buildMovie(movieDetail);
        // Actor 리스트 생성
        List<Actor> actorList = movieBuilderUtil.buildActorList(request);
        // Movie Actor 리스트 추가
        movie.addActors(actorList);
        // 데이터베이스에 저장
        movieRepoUtil.saveMovie(movie);
    }

    @Transactional
    public void updateMovie(MovieUpdateRequest request) {
        // 0. 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 영화 정보 업데이트
        MovieDetail movieDetail = movieBuilderUtil.buildMovieDetail(request); // MovieDetail 빌드
        movie.updateMovieDetail(movieDetail);
    }

    @Transactional
    public void updateMovieRating(MovieRatingUpdateRequest request) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 2. 영화 평점 업데이트
        movie.updateMovieRating(request.getMovieRating());
    }

    @Transactional
    public void deleteMovie(int movieSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. 영화 삭제
        movie.deleteMovie();
    }

    @Transactional
    public List<MovieListResponse> getAllMovieList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 1. 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findAll(pageable);
        // 2. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 1. 장르별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByGenre(genre, pageable);
        // 2. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByActor(String actorName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 1. 배우별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByActor(actorName, pageable);
        // 2. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    // TODO: ElasticSearch 활용 ( 검색 속도 개선 필요 )
    @Transactional
    public List<MovieListResponse> getMovieListByKeyword(String keyword, int userSeq, int page, int size) {
        // 1. MovieEvent 객체 생성
        UserActionEvent userActionEvent = MovieBuilderUtil.buildMovieEvent(userSeq, 0, keyword, "SEARCH", LocalDateTime.now());
        // 2. Kafka 이벤트 발행
        customProducer.send(userActionEvent);
        // 3. redis 키워드 조회 후 결과 반환
        String redisKey = keyword + "/" + page + "/" + size;
        List<MongoMovie> mongoMovieList = movieRepoUtil.findByKeywordForRedis(redisKey);
        if (mongoMovieList != null) {
            List<MovieListResponse> responses = new ArrayList<>();
            for (MongoMovie mongoMovie : mongoMovieList) {
                responses.add(new MovieListResponse(mongoMovie));
            }
            return responses;
        }
        // redis에 저장된 키워드가 없을 경우
        // 4. 키워드를 포함하는 영화 리스트 조회
        Pageable pageable = PageRequest.of(page, size);
        List<Movie> movieList = movieRepoUtil.findByKeyword(keyword, pageable);
        // 5. DB에서 가져온 결과 MongoDB에 저장 후 키 반환
        String mongoKey = movieRepoUtil.saveSearchListForMongoDB(movieList);
        // 6. Redis에 SearchResult 저장
        SearchResult searchResult = MovieBuilderUtil.buildSearchResult(redisKey, mongoKey);
        movieRepoUtil.saveSearchResult(searchResult);
        // 7. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public MovieDetailResponse getMovieDetail(int movieSeq, int userSeq) {
        // 1. MovieEvent 객체 생성
        UserActionEvent userActionEvent = MovieBuilderUtil.buildMovieEvent(userSeq, movieSeq, null, "DETAIL", LocalDateTime.now());
        // 2. Kafka 이벤트 발행
        customProducer.send(userActionEvent);
        // 3. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 4. MovieDetailResponse 생성
        return new MovieDetailResponse(movie, movie.getMovieDetail());
    }

    @Transactional
    public List<MovieListResponse> getRecommendationList(List<Integer> movieSeqList) {
        // 1. 추천된 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findBySeqIn(movieSeqList);
        // 2. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }
}
