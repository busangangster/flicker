package com.flicker.bff.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.movie.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class BffMovieService {

    private final Util util; // Util 클래스 의존성 주입

    private final ObjectMapper objectMapper; // ObjectMapper 클래스 의존성 주입

    @Value("${movie.baseurl}")
    private String movieBaseUrl; // 영화 서버 API의 기본 URL

    @Value("${user-review.baseurl}")
    private String userBaseUrl; // 사용자-리뷰 서버 API의 기본 URL

    @Value("${recommend.baseurl}")
    private String recommendBaseUrl; // 추천서버 API의 기본 URL


    public Mono<ResponseEntity<ResponseDto>> createMovie(MovieCreateRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/create");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsync(movieBaseUrl, path, request);
    }

    public Mono<ResponseEntity<ResponseDto>> updateMovie(MovieUpdateRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/update/detail");
        // 2. 비동기 방식으로 PUT 요청을 외부 API에 보냅니다.
        return util.sendPutRequestAsync(movieBaseUrl, path, request);
    }

    public Mono<ResponseEntity<ResponseDto>> deleteMovie(int movieSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/delete?movieSeq=" + movieSeq);
        // 2. 비동기 방식으로 PUT 요청을 외부 API에 보냅니다.
        return util.sendPutRequestAsync(movieBaseUrl, path, null);
    }

    public Mono<ResponseEntity<ResponseDto>> addActor(ActorAddRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/add/actor");
        // 2. 비동기 방식으로 POST 요청을 외부 API에 보냅니다.
        return util.sendPostRequestAsync(movieBaseUrl, path, request);
    }

    public Mono<ResponseEntity<ResponseDto>> deleteActor(int actorSeq, int movieSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/delete/actor/" + actorSeq + "/" + movieSeq);
        // 2. 비동기 방식으로 DELETE 요청을 외부 API에 보냅니다.
        return util.sendDeleteRequestAsync(movieBaseUrl, path);
    }


    public Mono<ResponseEntity<ResponseDto>> updateActor(ActorUpdateRequest request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/admin/update/actor");
        // 2. 비동기 방식으로 PUT 요청을 외부 API에 보냅니다.
        return util.sendPutRequestAsync(movieBaseUrl, path, request);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieList(int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieListByGenre(String genre, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/genre/" + genre + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieListByActor(String actorName, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/actor/" + actorName + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieListByCountry(String country, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/country/" + country + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieListByYear(int year, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/year/" + year + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieListBySearch(String keyword, int userSeq, int page, int size) {
        // 1. 검색 결과 가져오기
        String path = util.getUri("/list/search/" + keyword + "/" + userSeq + "/" + page + "/" + size);
        return util.sendGetRequestAsync(movieBaseUrl, path)
                .flatMap(getResponse -> {
                    ResponseDto searchResponseDto;
                    try {
                        searchResponseDto = Objects.requireNonNull(getResponse.getBody());
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "검색 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (searchResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(searchResponseDto.getHttpStatus(), searchResponseDto.getServiceStatus(), searchResponseDto.getMessage()),
                                searchResponseDto.getData()
                        ));
                    }
                    List<MovieListResponse> searchMovieListResponses;
                    try {
                        // searchResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                        searchMovieListResponses = objectMapper.convertValue(searchResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                        });
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "검색 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 검색 결과가 없을 경우 처리
                    if (searchMovieListResponses == null || searchMovieListResponses.isEmpty()) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_SUCH_ELEMENT, "검색 결과가 없습니다."));
                    }
                    // 2. 검색 한 영화와 추천서버에서 연관 영화 가져오기
                    List<RecommendByContentRequest> recommendByContentRequests = searchMovieListResponses.stream()
                            .map(movie -> new RecommendByContentRequest(movie.getMovieTitle(), movie.getMovieYear(), null))
                            .toList();
                    String recommendationPath = util.getUri("/content");
                    return util.sendPostRequestToRecommendServer(recommendBaseUrl, recommendationPath, recommendByContentRequests)
                            .flatMap(recommendResponse -> {
                                // 3. 사용자의 비선호 영화 목록을 가져옴
                                String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                                return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                        .flatMap(unlikeResponse -> {
                                            ResponseDto unlikeResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                unlikeResponseDto = Objects.requireNonNull(unlikeResponse.getBody());
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                        unlikeResponseDto.getData()
                                                ));
                                            }
                                            List<Integer> unlikeMovieSeqs;
                                            try {
                                                // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                                unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {
                                                });
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                            MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, unlikeMovieSeqs);
                                            String movieListPath = util.getUri("/list/recommendation");
                                            return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListPath, movieListRequest)
                                                    .flatMap(movieListResponse -> {
                                                        ResponseDto movieListResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "검색 영화 추천 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 상태 코드가 성공이 아닌 경우 처리
                                                        if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                    movieListResponseDto.getData()
                                                            ));
                                                        }
                                                        List<MovieListResponse> RecommendMovieListResponses;
                                                        try {
                                                            // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                            RecommendMovieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                            });
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "검색 영화 추천 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 5. 검색 영화와 추천된 영화 합치기
                                                        List<MovieListResponse> searchAndRecommendMovieListResponses = new ArrayList<>();
                                                        searchAndRecommendMovieListResponses.addAll(searchMovieListResponses);
                                                        searchAndRecommendMovieListResponses.addAll(RecommendMovieListResponses);
                                                        // 리스트의 크기를 20개로 제한
                                                        if (searchAndRecommendMovieListResponses.size() > 20) {
                                                            searchAndRecommendMovieListResponses = searchAndRecommendMovieListResponses.subList(0, 20);
                                                        }
                                                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, searchAndRecommendMovieListResponses));
                                                    });
                                        });
                            });
                });
    }

    public Mono<ResponseEntity<ResponseDto>> getTopMovieList() {
        // 1. 외부 API 경로 설정
        String path = util.getUri("/list/top10");
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMovieWordCloud(int movieSeq) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/wordCloud/" + movieSeq);
        // 2. 비동기 방식으로 POST 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    // 영화 상세조회
    public Mono<ResponseEntity<ResponseDto>> getMovieDetail(int movieSeq, int userSeq) {
        try {
            // 0. 응답 객체 생성
            MovieDetailAndReviewAndRecommendResponse movieDetailAndReviewAndRecommendResponse = new MovieDetailAndReviewAndRecommendResponse();
            // 1. 영화 서버에서 영화 상세 조회 결과 가져오기
            String movieDetailPath = util.getUri("/detail/" + movieSeq + "/" + userSeq);
            return util.sendGetRequestAsync(movieBaseUrl, movieDetailPath)
                    .flatMap(getResponse -> {
                        ResponseDto movieDetailResponseDto;
                        try {
                            movieDetailResponseDto = Objects.requireNonNull(getResponse.getBody());
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 상세정보 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                        }
                        // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                        if (movieDetailResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                            return Mono.error(new RestApiException(
                                    StatusCode.of(movieDetailResponseDto.getHttpStatus(), movieDetailResponseDto.getServiceStatus(), movieDetailResponseDto.getMessage()),
                                    movieDetailResponseDto.getData()
                            ));
                        }
                        // ObjectMapper를 이용하여 JSON 데이터를 MovieDetailResponse로 역직렬화
                        MovieDetailResponse movieDetailResponse;
                        try {
                            movieDetailResponse = objectMapper.convertValue(movieDetailResponseDto.getData(), MovieDetailResponse.class);
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 상세정보 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                        }
                        movieDetailAndReviewAndRecommendResponse.setMovieDetailResponse(movieDetailResponse);
                        // 2. 추천 서버에서 연관 영화 추천 가져오기
                        List<RecommendByContentRequest> recommendByContentRequests = Collections.singletonList(new RecommendByContentRequest(movieDetailResponse.getMovieTitle(), movieDetailResponse.getMovieYear(), null));
                        String recommendationPath = util.getUri("/content");
                        return util.sendPostRequestToRecommendServer(recommendBaseUrl, recommendationPath, recommendByContentRequests)
                                .flatMap(recommendResponse -> {
                                    // 3. 사용자의 찜/비선호 여부, 비선호 영화 목록, 탑 리뷰 목록을 가져옴
                                    String userMovieDetailPath = util.getUri("/movie-detail?userSeq=" + userSeq + "&movieSeq=" + movieSeq);
                                    return util.sendGetRequestAsync(userBaseUrl, userMovieDetailPath)
                                            .flatMap(userMovieDetailResponse -> {
                                                ResponseDto userMovieDetailDto;
                                                try {
                                                    // JSON 데이터를 ResponseDto로 역직렬화
                                                    userMovieDetailDto = Objects.requireNonNull(userMovieDetailResponse.getBody());
                                                } catch (Exception e) {
                                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                }
                                                // 상태 코드가 성공이 아닌 경우 처리
                                                if (userMovieDetailDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                    return Mono.error(new RestApiException(
                                                            StatusCode.of(userMovieDetailDto.getHttpStatus(), userMovieDetailDto.getServiceStatus(), userMovieDetailDto.getMessage()),
                                                            userMovieDetailDto.getData()
                                                    ));
                                                }
                                                UserMovieDetailResponse userMovieDetail;
                                                try {
                                                    // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                                    userMovieDetail = objectMapper.convertValue(userMovieDetailDto.getData(), UserMovieDetailResponse.class);
                                                } catch (Exception e) {
                                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 영화 상세 정보 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                }
                                                // 가져온 목록을 movieDetailReviewRecommendResponse에 설정
                                                movieDetailAndReviewAndRecommendResponse.setBookMarkedMovie(userMovieDetail.isBookMarkedMovie());
                                                movieDetailAndReviewAndRecommendResponse.setUnlikedMovie(userMovieDetail.isUnlikedMovie());
                                                for (ReviewResponse review : userMovieDetail.getReviews()) {
                                                    review.setTop(true);
                                                }
                                                movieDetailAndReviewAndRecommendResponse.setReviews(userMovieDetail.getReviews());
                                                // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, userMovieDetail.getUnlikedMovies());
                                                String movieListPath = util.getUri("/list/recommendation");

                                                return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListPath, movieListRequest)
                                                        .flatMap(movieListResponse -> {
                                                            ResponseDto movieListResponseDto;
                                                            try {
                                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                                movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                                            } catch (Exception e) {
                                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 상세 조회 연관 추천 영화 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                            }

                                                            
                                                            // 상태 코드가 성공이 아닌 경우 처리
                                                            if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                                return Mono.error(new RestApiException(
                                                                        StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                        movieListResponseDto.getData()
                                                                ));
                                                            }
                                                            List<MovieListResponse> movieListResponses;
                                                            try {
                                                                // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                                movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                                });
                                                            } catch (Exception e) {
                                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 상세 조회 연관 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                            }

                                                            // 변환된 목록을 movieDetailReviewRecommendResponse에 설정
                                                            movieDetailAndReviewAndRecommendResponse.setSimilarMovies(movieListResponses);
                                                            return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieDetailAndReviewAndRecommendResponse));
                                                        });
                                            });
                                });
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 서버에서 상세조회, 추천 서버에서 연관영화를 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 상세 정보 + 연관 영화 + 리뷰 조회 중 알 수 없는 오류가 발생: " + e.getMessage()));
        }
    }


    // 행동 기반 영화 추천
    public Mono<ResponseEntity<ResponseDto>> getActionRecommendationListAsync(int userSeq) {
        // 1. 영화 서버에서 사용자의 최근 행동을 가져옴 (10개)
        String path = util.getUri("/actions/" + userSeq);
        return util.sendGetRequestAsync(movieBaseUrl, path)
                .flatMap(getResponse -> {
                    ResponseDto userActionResponseDto;
                    try {
                        userActionResponseDto = Objects.requireNonNull(getResponse.getBody());
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 행동 데이터 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (userActionResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(userActionResponseDto.getHttpStatus(), userActionResponseDto.getServiceStatus(), userActionResponseDto.getMessage()),
                                userActionResponseDto.getData()
                        ));
                    }
                    // 데이터가 List<UserActionResponse> 타입인지 확인 후 변환
                    List<UserActionResponse> userActions;
                    try {
                        userActions = objectMapper.convertValue(userActionResponseDto.getData(), new TypeReference<List<UserActionResponse>>() {
                        });
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 행동 데이터 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 사용자의 최근 행동이 없을 경우 처리
                    if (userActions == null || userActions.isEmpty()) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_SUCH_ELEMENT, "사용자의 최근 행동이 없습니다."));
                    }
                    // 2. 추천 서버로 사용자의 최근 행동 목록을 전송하고, 추천 영화 목록을 가져옴
                    String recommendationPath = util.getUri("/content");
                    // 추천 서버에 요청할 때 사용자의 최근 행동 키워드만 추출하여 전송
                    List<RecommendByContentRequest> recommendByContentRequests = userActions.stream()
                            .map(userAction -> new RecommendByContentRequest(userAction.getKeyword(), userAction.getMovieYear(), null))
                            .toList();
                    return util.sendPostRequestToRecommendServer(recommendBaseUrl, recommendationPath, recommendByContentRequests)
                            .flatMap(recommendResponse -> {
                                // 3. 사용자의 비선호 영화 목록을 가져옴
                                String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                                return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                        .flatMap(unlikeResponse -> {
                                            ResponseDto unlikeResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                unlikeResponseDto = Objects.requireNonNull(unlikeResponse.getBody());
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                        unlikeResponseDto.getData()
                                                ));
                                            }
                                            List<Integer> unlikeMovieSeqs;
                                            try {
                                                // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                                unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {
                                                });
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                            MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, unlikeMovieSeqs);
                                            String movieListpath = util.getUri("/list/recommendation");
                                            return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListpath, movieListRequest)
                                                    .flatMap(movieListResponse -> {
                                                        ResponseDto movieListResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "행동 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 상태 코드가 성공이 아닌 경우 처리
                                                        if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                    movieListResponseDto.getData()
                                                            ));
                                                        }
                                                        List<MovieListResponse> movieListResponses;
                                                        try {
                                                            // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                            movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                            });
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "행동 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 변환된 목록을 반환
                                                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieListResponses));
                                                    });
                                        });
                            });
                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 서버에서 행동 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }


    // 사용자 평점-리뷰 기반 영화 추천
    public Mono<ResponseEntity<ResponseDto>> getReviewRecommendationList(int userSeq) {
        // 1. 추천서버에서 추천 영화 목록을 가져옴
        String path = util.getUri("/collabo");
        return util.sendPostRequestToRecommendServer(recommendBaseUrl, path, userSeq)
                .flatMap(recommendResponse -> {
                    System.out.println("recommendResponse = " + recommendResponse);
                    if (recommendResponse == null || recommendResponse.isEmpty()) {
                        // 선호도 영화 기반 추천
                        System.out.println("선호도 영화 기반 추천 호출됨");
                        return getRecommendationMovieListByLike(userSeq);
                    }
                    // 2. 사용자 서버에서 비선호 영화 목로 가져옴
                    String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                    return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                            .flatMap(unlikeResponse -> {
                                ResponseDto unlikeResponseDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    unlikeResponseDto = Objects.requireNonNull(unlikeResponse.getBody());
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                            unlikeResponseDto.getData()
                                    ));
                                }
                                List<Integer> unlikeMovieSeqs;
                                try {
                                    // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                    unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {
                                    });
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 3. 추천 영화 목록을 가져와서 영화 서버에 요청
                                MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, unlikeMovieSeqs);
                                String movieListPath = util.getUri("/list/recommendation");
                                return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListPath, movieListRequest)
                                        .flatMap(movieListResponse -> {
                                            ResponseDto movieListResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                        movieListResponseDto.getData()
                                                ));
                                            }
                                            List<MovieListResponse> movieListResponses;
                                            try {
                                                // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                });
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieListResponses));
                                        });
                            });
                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 서버에서 리뷰 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }

    // 사용자 선호도 기반 영화 추천
    private Mono<ResponseEntity<ResponseDto>> getRecommendationMovieListByLike(int userSeq) {
        // 1. 사용자 서버에서 선호도 영화 조회
        String likeMoviePath = util.getUri("/" + userSeq + "/favorite-movie");
        return util.sendGetRequestAsync(userBaseUrl, likeMoviePath)
                .flatMap(likeResponse -> {
                    ResponseDto likeResponseDto;
                    try {
                        // JSON 데이터를 ResponseDto로 역직렬화
                        likeResponseDto = Objects.requireNonNull(likeResponse.getBody());
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (likeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(likeResponseDto.getHttpStatus(), likeResponseDto.getServiceStatus(), likeResponseDto.getMessage()),
                                likeResponseDto.getData()
                        ));
                    }
                    List<Integer> likeMovieSeqs;
                    try {
                        // likeResponseDto의 데이터 필드를 List<Integer>로 변환
                        likeMovieSeqs = objectMapper.convertValue(likeResponseDto.getData(), new TypeReference<List<Integer>>() {
                        });
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 2. 영화서버에서 해당 영화의 정보를 가져옴
                    String listMoviePath = util.getUri("/list/movieId");
                    return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, listMoviePath, likeMovieSeqs)
                            .flatMap(listMovieResponse -> {
                                ResponseDto listMovieResponseDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    listMovieResponseDto = Objects.requireNonNull(listMovieResponse.getBody());
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (listMovieResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(listMovieResponseDto.getHttpStatus(), listMovieResponseDto.getServiceStatus(), listMovieResponseDto.getMessage()),
                                            listMovieResponseDto.getData()
                                    ));
                                }
                                List<MovieListResponse> movieListResponses;
                                try {
                                    // listMovieResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                    movieListResponses = objectMapper.convertValue(listMovieResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                    });
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 3. 추천 서버에서 연관 영화 목록을 가져옴
                                List<RecommendByContentRequest> recommendByContentRequests = movieListResponses.stream()
                                        .map(movie -> new RecommendByContentRequest(movie.getMovieTitle(), movie.getMovieYear(), null))
                                        .toList();
                                String recommendationPath = util.getUri("/content");
                                return util.sendPostRequestToRecommendServer(recommendBaseUrl, recommendationPath, recommendByContentRequests)
                                        .flatMap(recommendResponse -> {
                                            // 4. 사용자의 비선호 영화 목록을 가져옴
                                            String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                                            return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                                    .flatMap(unlikeResponse -> {
                                                        ResponseDto unlikeResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            unlikeResponseDto = Objects.requireNonNull(unlikeResponse.getBody());
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 상태 코드가 성공이 아닌 경우 처리
                                                        if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                                    unlikeResponseDto.getData()
                                                            ));
                                                        }
                                                        List<Integer> unlikeMovieSeqs;
                                                        try {
                                                            // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                                            unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {
                                                            });
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 5. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                        MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, unlikeMovieSeqs);
                                                        String movieListPath = util.getUri("/list/recommendation");
                                                        return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListPath, movieListRequest)
                                                                .flatMap(movieListResponse -> {
                                                                    ResponseDto movieListResponseDto;
                                                                    try {
                                                                        // JSON 데이터를 ResponseDto로 역직렬화
                                                                        movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                                                    } catch (Exception e) {
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호도 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                                    }
                                                                    // 상태 코드가 성공이 아닌 경우 처리
                                                                    if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                                        return Mono.error(new RestApiException(
                                                                                StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                                movieListResponseDto.getData()
                                                                        ));
                                                                    }
                                                                    List<MovieListResponse> RecommendMovieListResponses;
                                                                    try {
                                                                        // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                                        RecommendMovieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                                        });
                                                                    } catch (Exception e) {
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호도 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                                    }
                                                                    return Mono.just(ResponseDto.response(StatusCode.SUCCESS, RecommendMovieListResponses));
                                                                });
                                                    });
                                        });
                            });

                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "선호도 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }

    public Mono<ResponseEntity<ResponseDto>> getTopRatingMovieList() {
        // 1. 사용자 서버에서 리뷰개수가 2000개 이상인 영화 목록 조회
        String topRatingMoviePath = util.getUri("/review/most-reviews");
        return util.sendGetRequestAsync(userBaseUrl, topRatingMoviePath)
                .flatMap(topRatingMovieResponse -> {
                    ResponseDto topRatingMovieResponseDto;
                    try {
                        // JSON 데이터를 ResponseDto로 역직렬화
                        topRatingMovieResponseDto = Objects.requireNonNull(topRatingMovieResponse.getBody());
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 개수가 2000개 이상인 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (topRatingMovieResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(topRatingMovieResponseDto.getHttpStatus(), topRatingMovieResponseDto.getServiceStatus(), topRatingMovieResponseDto.getMessage()),
                                topRatingMovieResponseDto.getData()
                        ));
                    }
                    List<Integer> topRatingMovieSeqs;
                    try {
                        // topRatingMovieResponseDto의 데이터 필드를 List<Integer>로 변환
                        topRatingMovieSeqs = objectMapper.convertValue(topRatingMovieResponseDto.getData(), new TypeReference<List<Integer>>() {
                        });
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 개수가 2000개 이상인 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    String topMovieListPath = util.getUri("/list/topRating");
                    return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, topMovieListPath, topRatingMovieSeqs)
                            .flatMap(topMovieListResponse -> {
                                ResponseDto topMovieListResponseDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    topMovieListResponseDto = Objects.requireNonNull(topMovieListResponse.getBody());
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 개수가 2000개 이상이면서 평점이 높은 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (topMovieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(topMovieListResponseDto.getHttpStatus(), topMovieListResponseDto.getServiceStatus(), topMovieListResponseDto.getMessage()),
                                            topMovieListResponseDto.getData()
                                    ));
                                }
                                List<MovieListResponse> topMovieListResponses;
                                try {
                                    // topMovieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                    topMovieListResponses = objectMapper.convertValue(topMovieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                    });
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 개수가 2000개 이상이면서 평점이 높은 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, topMovieListResponses));
                            });
                });
    }

    public Mono<ResponseEntity<ResponseDto>> getRecommendationMovieListByActor(int userSeq) {
        // 1. 영화 서버에서 사용자의 추천 영화 배우 가져오기
        String path = util.getUri("/recommendActor/" + userSeq);
        return util.sendGetRequestAsync(movieBaseUrl, path)
                .flatMap(getResponse -> {
                    ResponseDto actorResponseDto;
                    try {
                        actorResponseDto = Objects.requireNonNull(getResponse.getBody());
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천 배우 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (actorResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(actorResponseDto.getHttpStatus(), actorResponseDto.getServiceStatus(), actorResponseDto.getMessage()),
                                actorResponseDto.getData()
                        ));
                    }
                    RecommendActorResponse recommendActorResponse;
                    try {
                        // actorListResponseDto의 데이터 필드를 String로 변환
                        recommendActorResponse = objectMapper.convertValue(actorResponseDto.getData(), new TypeReference<RecommendActorResponse>() {
                        });
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천 배우 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    if (recommendActorResponse == null) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_SUCH_ELEMENT, "최근에 리뷰를 달지 않았습니다."));
                    }
                    // 2. 추천 서버에서 연관 영화 목록을 가져옴
                    List<RecommendByContentRequest> recommendByContentRequests = Collections.singletonList(new RecommendByContentRequest(null, null, recommendActorResponse.getActorName()));
                    String recommendationPath = util.getUri("/content");
                    return util.sendPostRequestToRecommendServer(recommendBaseUrl, recommendationPath, recommendByContentRequests)
                            .flatMap(recommendResponse -> {
                                // 3. 사용자의 비선호 영화 목록을 가져옴
                                String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                                return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                        .flatMap(unlikeResponse -> {
                                            ResponseDto unlikeResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                unlikeResponseDto = Objects.requireNonNull(unlikeResponse.getBody());
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                        unlikeResponseDto.getData()
                                                ));
                                            }
                                            List<Integer> unlikeMovieSeqs;
                                            try {
                                                // unlikeResponseDto의 데이터 필드를 List<Integer>로 변환
                                                unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {
                                                });
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                            MovieListRequest movieListRequest = new MovieListRequest(recommendResponse, unlikeMovieSeqs);
                                            String movieListPath = util.getUri("/list/recommendation");
                                            return util.sendGetWithRequestBodyRequestAsync(movieBaseUrl, movieListPath, movieListRequest)
                                                    .flatMap(movieListResponse -> {
                                                        ResponseDto movieListResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            movieListResponseDto = Objects.requireNonNull(movieListResponse.getBody());
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천 배우 기반 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 상태 코드가 성공이 아닌 경우 처리
                                                        if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                    movieListResponseDto.getData()
                                                            ));
                                                        }
                                                        List<MovieListResponse> movieListResponses;
                                                        try {
                                                            // movieListResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                            movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {
                                                            });
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천 배우 기반 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        RecommendMovieByActorResponse recommendMovieByActorResponse = new RecommendMovieByActorResponse(recommendActorResponse.getActorName(), recommendActorResponse.getMovieTitle(), movieListResponses);
                                                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, recommendMovieByActorResponse));
                                                    });
                                        });
                            });
                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 서버에서 배우 기반 영화 추천 목록 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }

    public Mono<ResponseEntity<ResponseDto>> getNewMovieList() {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/newMovie");
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }

    public Mono<ResponseEntity<ResponseDto>> getMoviePoster(int movieSeq) {
        // 1. 영화 서버에서 영화 포스터 가져오기
        String path = util.getUri("/poster/" + movieSeq);
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }
}
