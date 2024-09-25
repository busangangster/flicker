package com.flicker.movie.movie.application;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.entity.*;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.UserActionResponse;
import com.flicker.movie.movie.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    * MovieService 클래스는 영화 정보를 생성, 수정, 삭제하는 비즈니스 로직을 담당한다.
    * MovieBuilderUtil, MovieRepoUtil, CustomProducer를 주입받는다.
    * createMovie() 메서드는 영화 정보를 생성한다.
    * updateMovie() 메서드는 영화 정보를 수정한다.
    * updateMovieRating() 메서드는 영화 평점을 수정한다.
    * deleteMovie() 메서드는 영화 정보를 삭제한다.
    * getAllMovieList() 메서드는 모든 영화 리스트를 조회한다.
    * getMovieListByGenre() 메서드는 장르별 영화 리스트를 조회한다.
    * getMovieListByActor() 메서드는 배우별 영화 리스트를 조회한다.
    * getMovieListByKeyword() 메서드는 키워드를 포함하는 영화 리스트를 조회한다.
    * getMovieDetail() 메서드는 영화 상세 정보를 조회한다.
    * getRecommendationList() 메서드는 추천된 영화 리스트를 조회한다.
    * getUserActionList() 메서드는 사용자 행동 로그를 조회한다.
    * maintainMaxUserActions() 메서드는 사용자 행동 로그 최대 10개 유지한다.
 */
@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieBuilderUtil movieBuilderUtil;
    private final MovieRepoUtil movieRepoUtil;
    private final CustomProducer customProducer;  // Kafka 프로듀서 주입

    @Transactional
    public void createMovie(MovieCreateRequest request) {
        // 1. 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // 2. MovieDetail 빌드
        MovieDetail movieDetail = movieBuilderUtil.buildMovieDetail(request);
        // 3. Movie 엔티티 생성
        Movie movie = movieBuilderUtil.buildMovie(movieDetail);
        // 4. Actor 리스트 생성
        List<Actor> actorList = movieBuilderUtil.buildActorList(request);
        // 5. Movie Actor 리스트 추가
        movie.addActors(actorList);
        // 6. 데이터베이스에 저장
        movieRepoUtil.saveMovie(movie);
        // 7. Kafka 이벤트 발행
        MovieInfoEvent movieInfoEvent = movieBuilderUtil.buildMovieInfoEvent(movie.getMovieSeq(), "MOVIE", "Create", LocalDateTime.now());
        customProducer.send(movieInfoEvent, "movieInfo");
    }

    @Transactional
    public void updateMovie(MovieUpdateRequest request) {
        // 1. 중복 영화 확인
        movieRepoUtil.isDuplicatedMovie(request.getMovieTitle(), request.getMovieYear());
        // 2. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(request.getMovieSeq());
        // 3. 영화 정보 업데이트
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
        // 3. Kafka 이벤트 발행
        MovieInfoEvent movieInfoEvent = movieBuilderUtil.buildMovieInfoEvent(movieSeq, "MOVIE", "Delete", LocalDateTime.now());
        customProducer.send(movieInfoEvent, "movieInfo");
    }

    @Transactional
    public List<MovieListResponse> getAllMovieList(int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findAll(pageable);
        // 3. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByGenre(String genre, int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 장르별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByGenre(genre, pageable);
        // 3. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByActor(String actorName, int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 배우별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByActor(actorName, pageable);
        // 3. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    // TODO: ElasticSearch 활용 ( 검색 속도 개선 필요 )
    @Transactional
    public List<MovieListResponse> getMovieListByKeyword(String keyword, int userSeq, int page, int size) {
        // 1. 사용자 행동 로그 최대 10개 유지
        maintainMaxUserActions(userSeq);
        // 2. MongoUserAction 객체 생성
        MongoUserAction mongoUserAction = movieBuilderUtil.buildMongoUserAction(userSeq, keyword, "SEARCH", LocalDateTime.now());
        // 3. MongoDB에 행동 로그 저장
        movieRepoUtil.saveUserActionForMongoDB(mongoUserAction);
        // 4. redis 키워드 조회 후 결과 반환
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
        // 5. 키워드를 포함하는 영화 리스트 조회
        Pageable pageable = PageRequest.of(page, size);
        List<Movie> movieList = movieRepoUtil.findByKeyword(keyword, pageable);
        // 6. DB에서 가져온 결과 MongoDB에 저장 후 키 반환
        String mongoKey = movieRepoUtil.saveSearchListForMongoDB(movieList);
        // 7. Redis에 SearchResult 저장
        SearchResult searchResult = MovieBuilderUtil.buildSearchResult(redisKey, mongoKey);
        movieRepoUtil.saveSearchResultForRedis(searchResult);
        // 8. MovieDetailResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public MovieDetailResponse getMovieDetail(int movieSeq, int userSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. 사용자 행동 로그 최대 10개 유지
        maintainMaxUserActions(userSeq);
        // 3. MovieEvent 객체 생성
        MongoUserAction mongoUserAction = movieBuilderUtil.buildMongoUserAction(userSeq, movie.getMovieDetail().getMovieTitle(), "DETAIL", LocalDateTime.now());
        // 4. MongoDB에 행동 로그 저장
        movieRepoUtil.saveUserActionForMongoDB(mongoUserAction);
        // 5. MovieDetailResponse 생성
        return new MovieDetailResponse(movie, movie.getMovieDetail(), movie.getActors(), movie.getWordClouds());
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

    @Transactional
    public List<UserActionResponse> getUserActionList(int userSeq) {
        // 1. 사용자 행동 로그 조회
        List<MongoUserAction> userActionList = movieRepoUtil.findUserActionListForMongoDB(userSeq);
        // 2. 사용자 행동 로그가 없을 경우
        if(userActionList == null || userActionList.isEmpty()) {
            throw new RestApiException(StatusCode.NO_CONTENT, "사용자 행동 로그가 존재하지 않습니다.");
        }
        // 3. UserActionResponse 리스트 생성
        return userActionList.stream()
                .map(UserActionResponse::new)
                .toList();
    }


    // 사용자 행동 로그 최대 10개 유지하기 위한 메서드
    private void maintainMaxUserActions(int userSeq) {
        // 1. MongoDB에서 해당 유저의 행동 로그 조회 (오래된 로그 먼저)
        List<MongoUserAction> userActions = movieRepoUtil.findUserActionAllForMongoDB(userSeq);
        // 2. 행동 로그가 10개 이상이면 가장 오래된 로그 삭제 (FIFO)
        if (userActions.size() >= 10) {
            MongoUserAction oldestAction = userActions.get(0);  // 가장 오래된 로그
            movieRepoUtil.deleteUserActionForMongoDB(oldestAction.getId());  // 가장 오래된 로그 삭제
        }
    }
}
