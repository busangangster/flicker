package com.flicker.movie.movie.application;

import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.domain.entity.*;
import com.flicker.movie.movie.domain.vo.MongoMovie;
import com.flicker.movie.movie.infrastructure.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
    private final RedisSearchResultRepository redisSearchResultRepository;
    private final MongoMovieListRepository mongoMovieListRepository;
    private final MongoUserActionRepository mongoUserActionRepository;
    private final MovieBuilderUtil movieBuilderUtil;
    private final RedisTopMovieRepository redisTopMovieRepository;

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
        Optional<Movie> movie = movieRepository.findByMovieDetail_MovieTitleAndMovieDetail_MovieYearAndDelYN(movieTitle, movieYear, "N");
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
    public List<Movie> findAll(Pageable pageable) {
        try {
            return movieRepository.findByDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc("N", pageable).getContent();
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
    public List<Movie> findByGenre(String genre, Pageable pageable) {
        try {
            return movieRepository.findByMovieDetail_GenreContainingAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(genre, "N", pageable).getContent();
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
    public List<Movie> findByActor(String actorName, Pageable pageable) {
        try {
            return movieRepository.findByActors_ActorNameAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(actorName, "N", pageable).getContent();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "배우별 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 국가의 영화 목록을 조회하는 메서드입니다.
     *
     * @param country 조회할 영화의 국가
     * @return 조회된 영화 목록
     * @throws RestApiException 국가별 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByCountry(String country, Pageable pageable) {
        try {
            return movieRepository.findByMovieDetail_CountryContainingAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(country, "N", pageable).getContent();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "국가별 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 연도의 영화 목록을 조회하는 메서드입니다.
     *
     * @param year 조회할 영화의 연도
     * @return 조회된 영화 목록
     * @throws RestApiException 연도별 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByYear(int year, Pageable pageable) {
        try {
            return movieRepository.findByMovieDetail_MovieYearAndDelYNOrderByMovieSeqDesc(year, "N", pageable).getContent();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "연도별 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 키워드를 포함하는 영화 목록을 조회하는 메서드입니다.
     *
     * @param keyword 조회할 키워드
     * @return 조회된 영화 목록
     * @throws RestApiException 키워드를 포함하는 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findByKeyword(String keyword, Pageable pageable) {
        try {
            return movieRepository.findByKeywordInTitlePlotActorGenre(keyword, "N", pageable);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "키워드를 포함하는 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis에 저장된 키워드를 사용하여 영화 목록을 조회하는 메서드입니다.
     *
     * @param redisKey 조회할 키워드
     * @return 조회된 영화 목록
     */
    public List<MongoMovie> findByKeywordForRedis(String redisKey) {
        try {
            Optional<RedisSearchResult> searchResultOptional = redisSearchResultRepository.findByKeyword(redisKey);
            // Redis에 키가 없으면 null 반환
            if (searchResultOptional.isEmpty()) {
                return null;  // 키 자체가 없을 때
            }
            // MongoDB에서 조회
            String mongoKey = searchResultOptional.get().getMongoKey();
            Optional<MongoMovieList> mongoMovieList = mongoMovieListRepository.findByMongoKey(mongoKey);
            // MongoMovies가 비어있는 경우 빈 리스트 반환
            return mongoMovieList.map(MongoMovieList::getMongoMovies).orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 저장된 키워드를 조회하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis Keyword를 저장하는 메서드입니다.
     *
     * @param redisSearchResult 저장할 영화 목록
     */
    public void saveSearchResultForRedis(RedisSearchResult redisSearchResult) {
        try {
            redisSearchResultRepository.save(redisSearchResult);
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

    /**
     * 영화 목록을 조회하는 메서드입니다.
     *
     * @param movieSeqs 조회할 영화의 ID 목록
     * @return 조회된 영화 목록
     * @throws RestApiException 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findBySeqIn(List<Integer> movieSeqs) {
        try {
            if(movieSeqs == null || movieSeqs.isEmpty()) {
                return Collections.emptyList();
            }
            return movieRepository.findByMovieSeqInAndDelYN(movieSeqs, "N");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 추천된 영화 목록을 조회하는 메서드입니다.
     *
     * @param movieSeqList 조회할 영화의 ID 목록
     * @return 조회된 영화 목록
     * @throws RestApiException 추천된 영화 목록 조회 중 오류가 발생할 경우 발생
     */
    public List<Movie> findBySeqInAndFilterUnlike(List<Integer> movieSeqList, List<Integer> unlikeMovieSeqList) {
        try {
            if(movieSeqList == null || movieSeqList.isEmpty()) {
                return Collections.emptyList();
            }
            if (unlikeMovieSeqList == null || unlikeMovieSeqList.isEmpty()) {
                // unlikeMovieSeqList가 비어 있을 때, NOT IN 조건을 제거하고 실행
                return movieRepository.findByMovieSeqInAndDelYN(movieSeqList, "N");
            } else {
                // unlikeMovieSeqList가 비어 있지 않을 때, 원래 메서드를 실행
                return movieRepository.findByMovieSeqInAndMovieSeqNotInAndDelYN(movieSeqList, unlikeMovieSeqList, "N");
            }
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천된 영화 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에 사용자 행동 로그를 저장하는 메서드입니다.
     *
     * @param mongoUserAction 저장할 사용자 행동 로그
     */
    public void saveUserActionForMongoDB(MongoUserAction mongoUserAction) {
        try {
            mongoUserActionRepository.save(mongoUserAction);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에 사용자 행동 로그를 저장하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에서 사용자 행동 로그를 조회하는 메서드입니다.
     *
     * @param userSeq 사용자의 ID
     * @return 조회된 사용자 행동 로그 목록
     * @throws RestApiException 사용자 행동 로그 조회 중 오류가 발생할 경우 발생
     */
    public List<MongoUserAction> findUserActionListForMongoDB(int userSeq) {
        try {
            Pageable pageable = PageRequest.of(0, 10);
            return mongoUserActionRepository.findByUserSeqOrderByTimestampDesc(userSeq, pageable);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에서 사용자의 최근 행동 로그를 조회하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에서 사용자 행동 로그를 조회하는 메서드입니다.
     *
     * @param userSeq 사용자의 ID
     * @return 조회된 사용자 행동 로그 목록
     * @throws RestApiException 사용자 행동 로그 조회 중 오류가 발생할 경우 발생
     */
    public List<MongoUserAction> findUserActionAllForMongoDB(int userSeq) {
        try {
            return mongoUserActionRepository.findByUserSeq(userSeq);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에서 사용자의 모든 행동 로그를 조회하는 중 오류가 발생했습니다.");
        }
    }


    /**
     * Redis에 저장된 검색 결과를 모두 삭제하는 메서드입니다.
     *
     * @throws RestApiException 검색 결과 삭제 중 오류가 발생할 경우 발생
     */
    public void deleteAllSearchResultForRedis() {
        try {
            redisSearchResultRepository.deleteAll();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 저장된 검색 결과를 초기화하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에 저장된 검색 결과를 모두 삭제하는 메서드입니다.
     *
     * @throws RestApiException 검색 결과 삭제 중 오류가 발생할 경우 발생
     */
    public void deleteAllSearchResultForMongoDB() {
        try {
            mongoMovieListRepository.deleteAll();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에 저장된 검색 결과를 초기화하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에서 모든 사용자의  최근 행동 로그를 조회하는 메서드입니다.
     *
     * @return 조회된 사용자 행동 로그 목록
     * @throws RestApiException 사용자 행동 로그 조회 중 오류가 발생할 경우 발생
     */
    public List<MongoUserAction> findUserActionsForMongoDB() {
        try {
            // 현재 시간에서 24시간 전 시간 계산
            LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusDays(1);
            // 레포지토리를 통해 24시간 내의 사용자 행동 로그 조회
            return mongoUserActionRepository.findByTimestampAfterAndActionOrderByTimestampDesc(twentyFourHoursAgo, "DETAIL");
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에서 모든 사용자의 최근 행동 로그를 조회하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 키워드로 영화 정보를 조회하는 메서드입니다.
     *
     * @param movieTitle 영화 제목
     * @return 조회된 영화 객체
     * @throws RestApiException 영화 정보 조회 중 오류가 발생할 경우 발생
     */
    public Movie findByMovieTitle(String movieTitle) {
        try {
            return movieRepository.findFirstByMovieDetail_MovieTitleAndDelYNOrderByMovieDetail_MovieYearDescMovieSeqDesc(movieTitle, "N")
                    .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, movieTitle + ": 해당 영화 정보를 찾을 수 없습니다."));
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "제목으로 영화 정보 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis에 Top10 영화 번호 목록을 저장하는 메서드입니다.
     *
     * @param redisTopMovie 저장할 Top10 영화 번호 목록
     * @throws RestApiException Top10 영화 번호 목록 저장 중 오류가 발생할 경우 발생
     */
    public void saveTopMovieForRedis(RedisTopMovie redisTopMovie) {
        try {
            redisTopMovieRepository.save(redisTopMovie);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 Top10 영화 번호 목록을 저장하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * Redis에 저장된 Top10 영화 번호 목록을 조회하는 메서드입니다.
     *
     * @return 조회된 Top10 영화 번호 목록
     * @throws RestApiException Top10 영화 번호 목록 조회 중 오류가 발생할 경우 발생
     */
    public RedisTopMovie findTopMovieListForRedis() {
        try {
            return redisTopMovieRepository.findById("TopMovieList")
                    .orElseThrow(() -> new RestApiException(StatusCode.NOT_FOUND, "Redis에서 Top10 영화 목록을 찾을 수 없습니다."));
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Redis에 저장된 Top10 영화 목록을 조회하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * MongoDB에서 오래된 사용자 행동 로그를 삭제하는 메서드입니다.
     *
     * @throws RestApiException 사용자 행동 로그 삭제 중 오류가 발생할 경우 발생
     */
    public void deleteUserActionsForMongoDB() {
        try {
            // 현재 시간에서 7일 전 시간 계산
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            // 레포지토리를 통해 7일 전의 사용자 행동 로그 삭제
            mongoUserActionRepository.deleteByTimestampBefore(sevenDaysAgo);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MongoDB에서 오래된 사용자 행동 로그를 삭제하는 중 오류가 발생했습니다.");
        }
    }
}
