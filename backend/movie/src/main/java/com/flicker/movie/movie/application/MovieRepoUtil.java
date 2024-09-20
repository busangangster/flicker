package com.flicker.movie.movie.application;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.domain.entity.MongoMovieList;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.domain.entity.SearchResult;
import com.flicker.movie.movie.infrastructure.MongoMovieListRepository;
import com.flicker.movie.movie.infrastructure.MovieRepository;
import com.flicker.movie.movie.infrastructure.SearchResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * MovieRepoUtil은 MovieRepository를 감싸서 영화 정보를 조회하고 저장하는
 * 공통 기능을 제공하는 유틸리티 클래스입니다.
 * <p>
 * 이 클래스는 주로 영화 정보에 대한 데이터베이스 조회 및 저장 작업을 처리하며,
 * 예외 처리를 포함하여 공통된 로직을 재사용할 수 있도록 설계되었습니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MovieRepoUtil {

    private final MovieRepository movieRepository;
    private final SearchResultRepository searchResultRepository;
    private final MongoMovieListRepository mongoMovieListRepository;
    private final MovieBuilderUtil movieBuilderUtil;

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

    /**
     * 영화 전체 목록을 조회하는 메서드입니다.
     *
     * @return 조회된 영화 목록
     * @throws RestApiException 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findAll() {
        try {
            return movieRepository.findByDelYNOrderByMovieDetail_MovieYearDesc("N");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 전체 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 장르의 영화 목록을 조회하는 메서드입니다.
     *
     * @param genre 조회할 영화의 장르
     * @return 조회된 영화 목록
     * @throws RestApiException 장르별 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByGenre(String genre) {
        try {
            return movieRepository.findByMovieDetail_GenreContainingAndDelYNOrderByMovieDetail_MovieYearDesc(genre, "N");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "장르별 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 배우가 출연한 영화 목록을 조회하는 메서드입니다.
     *
     * @param actorName 조회할 배우의 이름
     * @return 조회된 영화 목록
     * @throws RestApiException 배우별 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByActor(String actorName) {
        try {
            return movieRepository.findByActors_ActorNameContainingAndDelYNOrderByMovieDetail_MovieYearDesc(actorName, "N");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "배우별 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 키워드를 포함하는 영화 목록을 조회하는 메서드입니다.
     *
     * @param keyword 조회할 키워드
     * @return 조회된 영화 목록
     * @throws RestApiException 키워드를 포함하는 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByKeyword(String keyword) {
        try {
            return movieRepository.findByKeywordInTitlePlotActorGenre(keyword, "N");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "키워드를 포함하는 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis에 저장된 키워드를 사용하여 영화 목록을 조회하는 메서드입니다.
     *
     * @param keyword 조회할 키워드
     * @return 조회된 영화 목록
     */
    public List<MongoMovie> findByKeywordForRedis(String keyword) {
        try {
            Optional<SearchResult> searchResultOptional = searchResultRepository.findByKeyword(keyword);
            // Redis에 값이 없으면 null 반환
            if (searchResultOptional.isEmpty()) {
                return null;
            }
            // MongoDB에서 조회
            String mongoKey = searchResultOptional.get().getMongoKey();
            Optional<MongoMovieList> mongoMovieList = mongoMovieListRepository.findByMongoKey(mongoKey);
            return mongoMovieList.map(MongoMovieList::getMongoMovies).orElse(null);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 저장된 키워드를 조회하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis Keyword를 저장하는 메서드입니다.
     *
     * @param searchResult 저장할 영화 목록
     */
    public void saveSearchResult(SearchResult searchResult) {
        try {
            searchResultRepository.save(searchResult);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 검색 결과를 저장하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에 검색 결과를 저장하는 메서드입니다.
     *
     * @param movieList 저장할 영화 목록
     * @return 저장된 MongoDB의 키
     */
    public String saveSearchListForMongoDB(List<Movie> movieList) {
        try {
            MongoMovieList mongoMovieList = movieBuilderUtil.buildMongoMovieList(movieList);
            return mongoMovieListRepository.save(mongoMovieList).getMongoKey();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에 검색 결과를 저장하는 중 오류가 발생했습니다.");
        }
    }
}
