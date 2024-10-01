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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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


    public Mono<ResponseEntity<ResponseDto>> updateActor(ActorAddRequest request) {
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
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/search/" + keyword + "/" + userSeq + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
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

    // 임의로 Top10 영화 목록을 설정하는 메서드 (임시)
    public Mono<ResponseEntity<ResponseDto>> setTopMovieList(List<Integer> movieSeqs) {
        // 1. 외부 API 경로 설정
        String path = util.getUri("/admin/set/top10");
        // 2. POST 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendPostRequestAsync(movieBaseUrl, path, movieSeqs);
    }


    // TODO: 영화 상세조회 / 추천 서버 ( API 경로 수정, DTO 매핑 )
    public Mono<ResponseEntity<ResponseDto>> getMovieDetail(int movieSeq, int userSeq) {
        try {
            // 0. 응답 객체 생성
            MovieDetailReviewRecommendResponse movieDetailReviewRecommendResponse = new MovieDetailReviewRecommendResponse();
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
                        movieDetailReviewRecommendResponse.setMovieDetail(movieDetailResponse);
                        // 2. 연관 영화 추천 가져오기
                        String recommendationPath = util.getUri("/list/recommendation/similar/" + movieDetailResponse.getMovieTitle());
                        return util.sendGetRequestAsync(recommendBaseUrl, recommendationPath)
                                .flatMap(recommendResponse -> {
                                    String movieSeqListResponseDtoJson;
                                    try {
                                        movieSeqListResponseDtoJson = Objects.requireNonNull(recommendResponse.getBody()).toString();
                                    } catch (Exception e) {
                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieSeqListResponseDtoJson: recommendResponse.getBody()가 null입니다." + e.getMessage()));
                                    }
                                    ResponseDto movieSeqListResponseDto;
                                    try {
                                        movieSeqListResponseDto = objectMapper.readValue(movieSeqListResponseDtoJson, ResponseDto.class);
                                    } catch (Exception e) {
                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "연관 추천 영화 번호 목록 Body을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                    }
                                    if (movieSeqListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                        return Mono.error(new RestApiException(
                                                StatusCode.of(movieSeqListResponseDto.getHttpStatus(), movieSeqListResponseDto.getServiceStatus(), movieSeqListResponseDto.getMessage()),
                                                movieSeqListResponseDto.getData()
                                        ));
                                    }
                                    List<Integer> movieSeqList;
                                    try {
                                        movieSeqList = objectMapper.convertValue(movieSeqListResponseDto.getData(), new TypeReference<List<Integer>>() {});
                                    } catch (Exception e) {
                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "연관 추천 영화 번호 목록 데이터을 역직렬화하는데 오류 발생: " + e.getMessage()));
                                    }
                                    // 3. 사용자의 찜/비선호 여부, 비선호 영화 목록을 가져옴
                                    String userMovieDetailPath = util.getUri("/movie-detail?userSeq=" + userSeq + "&movieSeq=" + movieSeq);
                                    return util.sendGetRequestAsync(movieBaseUrl, userMovieDetailPath)
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
                                                movieDetailReviewRecommendResponse.setBookMarkedMovie(userMovieDetail.isBookMarkedMovie());
                                                movieDetailReviewRecommendResponse.setUnlikedMovie(userMovieDetail.isUnlikedMovie());
                                                movieDetailReviewRecommendResponse.setReviews(userMovieDetail.getReviews());
                                                // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, userMovieDetail.getUnlikedMovies());
                                                String movieListPath = util.getUri("/list/recommendation");
                                                return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
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
                                                                movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {});
                                                            } catch (Exception e) {
                                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 상세 조회 연관 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                            }
                                                            // 변환된 목록을 movieDetailReviewRecommendResponse에 설정
                                                            movieDetailReviewRecommendResponse.setSimilarMovies(movieListResponses);
                                                            return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieDetailReviewRecommendResponse));
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


    // TODO: 행동 기반 영화 추천 / 추천서버 ( API 경로 수정, DTO 매핑 )
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
                        userActions = objectMapper.convertValue(userActionResponseDto.getData(), new TypeReference<List<UserActionResponse>>() {});
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 행동 데이터 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 사용자의 최근 행동이 없을 경우 처리
                    if (userActions == null || userActions.isEmpty()) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_CONTENT, "사용자의 최근 행동이 없습니다."));
                    }
                    // 2. 추천 서버로 사용자의 최근 행동 목록을 전송하고, 추천 영화 목록을 가져옴
                    String recommendationPath = util.getUri("/list/recommendation/action");
                    return util.sendPostRequestAsync(recommendBaseUrl, recommendationPath, userActions)
                            .flatMap(postResponse -> {
                                ResponseDto movieSeqListDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    movieSeqListDto = Objects.requireNonNull(postResponse.getBody());
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "행동 기반 추천 영화 시퀀스 데이터 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (movieSeqListDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(movieSeqListDto.getHttpStatus(), movieSeqListDto.getServiceStatus(), movieSeqListDto.getMessage()),
                                            movieSeqListDto.getData()
                                    ));
                                }
                                List<Integer> movieSeqList;
                                try {
                                    // movieSeqListDto의 데이터 필드를 List<Integer>로 변환
                                    movieSeqList = objectMapper.convertValue(movieSeqListDto.getData(), new TypeReference<List<Integer>>() {});
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "행동 기반 추천 영화 시퀀스 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 3. 사용자의 비선호 영화 목록을 가져옴
                                String unlikeMoviePath = util.getUri("/" + userSeq + "/unlike-movie");
                                return util.sendGetRequestAsync(movieBaseUrl, unlikeMoviePath)
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
                                                unlikeMovieSeqs = objectMapper.convertValue(unlikeResponseDto.getData(), new TypeReference<List<Integer>>() {});
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "비선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                            RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                            String movieListpath = util.getUri("/list/recommendation");
                                            return util.sendPostRequestAsync(movieBaseUrl, movieListpath, recommendMovieListRequest)
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
                                                            movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {});
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


    // TODO: 사용자 평점-리뷰 기반 영화 추천 / 유저서버, 추천서버 ( API경로 수정, DTO 매핑 )
    public Mono<ResponseEntity<ResponseDto>> getReviewRecommendationList(int userSeq) {
        // 1. 추천서버에 userSeq를 보내서 List<Integer> movieSeqs를 가져옴.
        String path = util.getUri("/list/recommendation/review/" + userSeq);
        return util.sendGetRequestAsync(recommendBaseUrl, path)
                .flatMap(getResponse -> {
                    // 2. 사용자 서버에서 비선호 영화 목로 가져옴
                    // 3. 영화 서버에서 영화 정보 목록을 가져옴
                    return Mono.just(ResponseDto.response(StatusCode.SUCCESS,null));
                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 서버에서 리뷰 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }
}
