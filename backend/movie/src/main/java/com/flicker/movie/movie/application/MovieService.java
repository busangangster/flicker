package com.flicker.movie.movie.application;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.entity.*;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.vo.MovieDetail;
import com.flicker.movie.movie.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // 7. redis , mongoDB (키워드 검색결과 ) 초기화
        initSearchResultForRedisAndMongoDB();
        // 8. Kafka 이벤트 발행
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
        // 2. Top10 영화 리스트 조회
        RedisTopMovie redisTopMovie = movieRepoUtil.findTopMovieListForRedis();
        // 3. 영화 삭제
        movie.deleteMovie(redisTopMovie);
        // 4. redis , mongoDB (키워드 검색결과 ) 초기화
        initSearchResultForRedisAndMongoDB();
        // 5. Kafka 이벤트 발행
        MovieInfoEvent movieInfoEvent = movieBuilderUtil.buildMovieInfoEvent(movieSeq, "MOVIE", "Delete", LocalDateTime.now());
        customProducer.send(movieInfoEvent, "movieInfo");
    }

    @Transactional
    public List<MovieListResponse> getAllMovieList(int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findAll(pageable);
        // 3. MovieListResponse 리스트 생성
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
        // 3. MovieListResponse 리스트 생성
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
        // 3. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByCountry(String country, int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 국가별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByCountry(country, pageable);
        // 3. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByYear(int year, int page, int size) {
        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 2. 연도별 영화 리스트 조회
        List<Movie> movieList = movieRepoUtil.findByYear(year, pageable);
        // 3. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();

    }

    // TODO: ElasticSearch 활용 ( 검색 속도 개선 필요 )
    @Transactional
    public List<MovieListResponse> getMovieListByKeyword(String keyword, int userSeq, int page, int size) {
        // 1. MongoUserAction 객체 생성
        MongoUserAction mongoUserAction = movieBuilderUtil.buildMongoUserAction(userSeq, keyword, "SEARCH", LocalDateTime.now(), null);
        // 2. MongoDB에 행동 로그 저장
        movieRepoUtil.saveUserActionForMongoDB(mongoUserAction);
        // 3. redis 키워드 조회 후 결과 반환
        String redisKey = keyword + "/" + page + "/" + size;
        List<MongoMovie> mongoMovieList = movieRepoUtil.findByKeywordForRedis(redisKey);
        if (mongoMovieList != null) {
            // redis에 저장된 키워드가 있을 경우
            // 4. MovieListResponse 리스트 생성
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
        RedisSearchResult redisSearchResult = MovieBuilderUtil.buildSearchResult(redisKey, mongoKey);
        movieRepoUtil.saveSearchResultForRedis(redisSearchResult);
        // 7. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public MovieDetailResponse getMovieDetail(int movieSeq, int userSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. MovieEvent 객체 생성
        MongoUserAction mongoUserAction = movieBuilderUtil.buildMongoUserAction(userSeq, movie.getMovieDetail().getMovieTitle(), "DETAIL", LocalDateTime.now(), movie.getMovieDetail().getMovieYear());
        // 3. MongoDB에 행동 로그 저장
        movieRepoUtil.saveUserActionForMongoDB(mongoUserAction);
        // 4. MovieDetailResponse 생성
        return new MovieDetailResponse(movie, movie.getMovieDetail(), movie.getActors());
    }

    @Transactional
    public List<MovieListResponse> getRecommendationList(RecommendMovieListRequest request) {
        // 1. 추천된 영화 리스트 조회 및 비선호 영화 필터링
        List<Movie> movieList = request.getMovieSeqListRequest().stream()
                .map(seqRequest -> movieRepoUtil.findByMovieTitleAndYear(seqRequest.getMovieTitle(), seqRequest.getMovieYear()))
                .filter(movie -> !request.getUnlikeMovieSeqList().contains(movie.getMovieSeq())) // 비선호 영화 필터링
                .toList();
        // 2. MovieListResponse 리스트 생성 및 반환
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserActionResponse> getUserActionList(int userSeq) {
        // 1. 사용자 행동 로그 조회
        List<MongoUserAction> userActionList = movieRepoUtil.findUserActionListForMongoDB(userSeq);
        // 2. 사용자 행동 로그가 없을 경우
        if (userActionList == null || userActionList.isEmpty()) {
            throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "사용자 행동 로그가 존재하지 않습니다.");
        }
        // 3. UserActionResponse 리스트 생성
        return userActionList.stream()
                .map(UserActionResponse::new)
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getTopMovieList() {
        // 1. Redis에서 TopMovieList 조회
        RedisTopMovie redisTopMovie = movieRepoUtil.findTopMovieListForRedis();
        List<Integer> movieSeqs;
        // Redis에 값이 없으면 DB에서 조회
        if (redisTopMovie == null) {
            List<TopMovie> topMovies = movieRepoUtil.findTopMovieList();
            if (topMovies.isEmpty()) {
                throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "Top 영화가 존재하지 않습니다.");
            }
            // 영화 번호 목록 추출
            movieSeqs = topMovies.stream()
                    .map(TopMovie::getMovieSeq)
                    .toList();
            // Redis에 영화 번호 목록을 저장 (키는 예를 들어 "TopMovieList")
            redisTopMovie = movieBuilderUtil.redisTopMovieBuilder("TopMovieList", movieSeqs);
            movieRepoUtil.saveTopMovieForRedis(redisTopMovie);
        }
        // 2. 영화 번호 목록 추출
        movieSeqs = redisTopMovie.getMovieSeqs();
        if (movieSeqs.isEmpty()) {
            movieRepoUtil.deleteTopMovieForRedis();
            throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "Top 영화가 존재하지 않습니다.");
        }
        // 3. TopMovieList 조회
        List<Movie> movieList = movieRepoUtil.findBySeqIn(movieSeqs);
        // 4. movieSeq를 키로 하는 Map으로 변환 (Movie 객체 매핑)
        Map<Integer, Movie> movieMap = movieList.stream()
                .collect(Collectors.toMap(Movie::getMovieSeq, Function.identity()));
        // 5. movieSeqs 순서에 맞춰 movieMap에서 Movie 객체를 가져와 List 생성
        List<Movie> orderedMovieList = new ArrayList<>();
        for (Integer seq : movieSeqs) {
            if (movieMap.containsKey(seq)) {
                orderedMovieList.add(movieMap.get(seq));
            }
        }
        // 6. MovieListResponse 리스트 생성
        return orderedMovieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getMovieListByMovieSeqList(List<Integer> request) {
        // 1. 영화 ID 리스트로 영화 목록 조회
        List<Movie> movieList = movieRepoUtil.findBySeqIn(request);
        // 2. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public List<WordCloudResponse> getWordCloud(int movieSeq) {
        // 1. 영화 정보 조회
        Movie movie = movieRepoUtil.findById(movieSeq);
        // 2. 워드 클라우드 조회
        List<WordCloud> wordClouds = movie.getWordClouds();
        // 3. WordCloud 정렬
        wordClouds.sort((o1, o2) -> o2.getCount() - o1.getCount());
        // 4. WordCloudResponse 리스트 생성 ( 상위 12개 )
        return wordClouds.stream()
                .limit(12)
                .map(WordCloudResponse::new)
                .toList();
    }

    @Transactional
    public List<MovieListResponse> getTopRatingMovieList() {
        // 1. 평점 높은 영화 30개 조회
        Pageable pageable = PageRequest.of(0, 30);
        List<Movie> movieList = movieRepoUtil.findTopRatingMovieList(pageable);
        // 2. MovieListResponse 리스트 생성
        return movieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

    @Transactional
    public void deleteTopMovie() {
        movieRepoUtil.deleteTopMovie();
        movieRepoUtil.deleteTopMovieForRedis();
    }

    // redis , mongoDB (키워드 검색결과 ) 초기화
    private void initSearchResultForRedisAndMongoDB() {
        movieRepoUtil.deleteAllSearchResultForRedis();
        movieRepoUtil.deleteAllSearchResultForMongoDB();
    }

    // Top10 키워드 추출
    @Transactional
    public List<String> findTopKeywords(List<MongoUserAction> userActions) {
        // 키워드의 빈도를 계산하고 상위 10개를 추출
        return userActions.stream()
                .collect(Collectors.groupingBy(MongoUserAction::getKeyword, Collectors.counting())) // 키워드 빈도수 계산
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // 빈도수 내림차순 정렬
                .limit(10) // 상위 10개 추출
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 영화 제목 목록의 영화 번호 추출
    @Transactional
    public List<Integer> findMovieSeqsByKeywords(List<String> movieTitles) {
        List<Integer> movieSeqs = new ArrayList<>();
        for (String movieTitle : movieTitles) {
            // 영화 제목으로 영화 번호 조회
            Movie movie = movieRepoUtil.findByMovieTitle(movieTitle);
            // 영화 번호 목록에 추가
            movieSeqs.add(movie.getMovieSeq());
        }
        return movieSeqs;
    }

    @Transactional
    public RecommendActorResponse getRecommendActor(int userSeq) {
        // 랜덤으로 해당 유저의 사용자 추천 배우 중 1명 뽑기
        List<RecommendActor> recommendActors = movieRepoUtil.findRecommendActor(userSeq);
        if (recommendActors == null || recommendActors.isEmpty()) {
            throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "추천된 배우가 존재하지 않습니다.");
        }
        RecommendActor recommendActor = recommendActors.get((int) (Math.random() * recommendActors.size()));
        Movie movie = movieRepoUtil.findById(recommendActor.getMovieSeq());
        Actor actor = movie.getActor(recommendActor.getActorSeq());
        return new RecommendActorResponse(actor.getActorName(), movie.getMovieDetail().getMovieTitle());
    }

    @Transactional
    public void deleteNewMovie() {
        movieRepoUtil.deleteNewMovie();
        movieRepoUtil.deleteNewMovieForRedis();
    }

    @Transactional
    public void saveNewMovie(List<Integer> movieSeqList) {
        List<NewMovie> newMovies = movieBuilderUtil.buildNewMovieList(movieSeqList);
        movieRepoUtil.saveNewMovie(newMovies);
    }

    @Transactional
    public List<MovieListResponse> getNewMovieList() {
        // 1. redis에서 개봉 영화 목록 조회
        RedisNewMovie redisNewMovie = movieRepoUtil.findNewMovieForRedis();
        List<Integer> movieSeqs;
        // 2. redis에 값이 없으면 DB에서 조회
        if (redisNewMovie == null) {
            // 1. 개봉 영화 목록 조회
            List<NewMovie> newMovies = movieRepoUtil.findNewMovie();
            if (newMovies.isEmpty()) {
                System.out.println("개봉 영화가 존재하지 않습니다.");
                throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "개봉 영화가 존재하지 않습니다.");
            }
            // 2. 영화 번호 목록 추출
            movieSeqs = newMovies.stream()
                    .map(NewMovie::getMovieSeq)
                    .toList();
            // 3. redis에 영화 번호 목록 저장
            redisNewMovie = movieBuilderUtil.redisNewMovieBuilder("NewMovieList", movieSeqs);
            movieRepoUtil.saveNewMovieForRedis(redisNewMovie);
        }
        // 3. redis에 값이 있으면 redis에서 조회
        movieSeqs = redisNewMovie.getMovieSeqs();
        if (movieSeqs.isEmpty()) {
            movieRepoUtil.deleteNewMovieForRedis();
            throw new RestApiException(StatusCode.NO_SUCH_ELEMENT, "개봉 영화가 존재하지 않습니다.");
        }
        // 4. 개봉 영화 목록 조회
        List<Movie> movieList = movieRepoUtil.findBySeqIn(movieSeqs);
        // 5. 검색 Redis, MongoDB 초기화
        initSearchResultForRedisAndMongoDB();
        // 4. movieSeq를 키로 하는 Map으로 변환 (Movie 객체 매핑)
        Map<Integer, Movie> movieMap = movieList.stream()
                .collect(Collectors.toMap(Movie::getMovieSeq, Function.identity()));
        // 5. movieSeqs 순서에 맞춰 movieMap에서 Movie 객체를 가져와 List 생성
        List<Movie> orderedMovieList = new ArrayList<>();
        for (Integer seq : movieSeqs) {
            if (movieMap.containsKey(seq)) {
                orderedMovieList.add(movieMap.get(seq));
            }
        }
        // 6. MovieListResponse 리스트 생성
        return orderedMovieList.stream()
                .map(movie -> new MovieListResponse(movie, movie.getMovieDetail()))
                .toList();
    }

}
