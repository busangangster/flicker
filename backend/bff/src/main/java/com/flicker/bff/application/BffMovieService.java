package com.flicker.bff.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Mono<ResponseEntity<ResponseDto>> getMovieListBySearch(String keyword, int userSeq, int page, int size) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/list/search/" + keyword + "/" + userSeq + "/" + page + "/" + size);
        // 2. 비동기 방식으로 GET 요청을 외부 API에 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }


    // TODO: 영화 상세조회 + 비선호영화 조회 + 연관 영화 추천  + 좋아요 높은 리뷰 조회 ( 경로 수정 )
    public Mono<ResponseEntity<ResponseDto>> getMovieDetail(int movieSeq, int userSeq) {
        try {
            // 0. 응답 객체 생성
            MovieDetailReviewRecommendResponse movieDetailReviewRecommendResponse = new MovieDetailReviewRecommendResponse();
            // 1. 영화 서버에서 영화 상세 조회 결과 가져오기
            String movieDetailPath = util.getUri("/detail/" + movieSeq + "/" + userSeq);
            Mono<ResponseEntity<ResponseDto>> movieDetailMono = util.sendGetRequestAsync(movieBaseUrl, movieDetailPath)
                    .flatMap(getResponse -> {
                        String movieDetailResponseDtoJson;
                        try {
                            movieDetailResponseDtoJson = Objects.requireNonNull(getResponse.getBody()).toString();
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieDetailResponseDtoJson: getResponse.getBody()가 null입니다." + e.getMessage()));
                        }
                        ResponseDto movieDetailResponseDto;
                        try {
                            movieDetailResponseDto = objectMapper.readValue(movieDetailResponseDtoJson, ResponseDto.class);
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
                                    // 3. 추천 영화 목록을 가져와서 영화 서버에 요청
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
                                    // 4. 사용자의 비선호 영화 목록을 가져옴
                                    String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                    return util.sendGetRequestAsync(movieBaseUrl, unlikeMoviePath)
                                            .flatMap(unlikeResponse -> {
                                                String unlikeResponseDtoJson;
                                                try {
                                                    // unlikeResponse.getBody()가 null인지 확인하고 toString() 처리
                                                    unlikeResponseDtoJson = Objects.requireNonNull(unlikeResponse.getBody()).toString();
                                                } catch (Exception e) {
                                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "UnlikeResponseDtoJson: unlikeResponse.getBody()가 null입니다." + e.getMessage()));
                                                }
                                                ResponseDto unlikeResponseDto;
                                                try {
                                                    // JSON 데이터를 ResponseDto로 역직렬화
                                                    unlikeResponseDto = objectMapper.readValue(unlikeResponseDtoJson, ResponseDto.class);
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
                                                // 5. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                                String movieListPath = util.getUri("/list/recommendation");
                                                return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
                                                        .flatMap(movieListResponse -> {
                                                            String movieListResponseDtoJson;
                                                            try {
                                                                // movieListResponse.getBody()가 null인지 확인하고 toString() 처리
                                                                movieListResponseDtoJson = Objects.requireNonNull(movieListResponse.getBody()).toString();
                                                            } catch (Exception e) {
                                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieListResponseDtoJson: movieListResponse.getBody()가 null입니다." + e.getMessage()));
                                                            }
                                                            ResponseDto movieListResponseDto;
                                                            try {
                                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                                movieListResponseDto = objectMapper.readValue(movieListResponseDtoJson, ResponseDto.class);
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
                                                            return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "영화 상세 조회 및 연관 영화 조회 성공"));
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

            // 6. 유저-리뷰 서버에서 좋아요 높은 리뷰 조회 (비동기로 병렬 처리)
            String userReviewPath = util.getUri("/list/review/top/" + movieSeq);
            Mono<ResponseEntity<ResponseDto>> reviewMono = util.sendGetRequestAsync(userBaseUrl, userReviewPath)
                    .flatMap(reviewResponse -> {
                        String reviewResponseDtoJson;
                        try {
                            // reviewResponse.getBody()가 null인지 확인하고 toString() 처리
                            reviewResponseDtoJson = Objects.requireNonNull(reviewResponse.getBody()).toString();
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "ReviewResponseDtoJson: reviewResponse.getBody()가 null입니다." + e.getMessage()));
                        }
                        ResponseDto reviewResponseDto;
                        try {
                            // JSON 데이터를 ResponseDto로 역직렬화
                            reviewResponseDto = objectMapper.readValue(reviewResponseDtoJson, ResponseDto.class);
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "유저-리뷰 서버에서 좋아요 높은 리뷰 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                        }
                        // 상태 코드가 성공이 아닌 경우 처리
                        if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                            return Mono.error(new RestApiException(
                                    StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                                    reviewResponseDto.getData()
                            ));
                        }
                        List<ReviewResponse> topReviews;
                        try {
                            // reviewResponseDto의 데이터 필드를 List<ReviewResponse>로 변환
                            topReviews = objectMapper.convertValue(reviewResponseDto.getData(), new TypeReference<List<ReviewResponse>>() {});
                        } catch (Exception e) {
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "유저-리뷰 서버에서 좋아요 높은 리뷰 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                        }
                        // 변환된 리뷰 목록을 movieDetailReviewRecommendResponse에 설정
                        movieDetailReviewRecommendResponse.setReviews(topReviews);
                        // 성공 응답 반환
                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "좋아요 높은 리뷰 가져오기 성공"));
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "유저-리뷰 서버에서 좋아요 높은 리뷰를 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
            // 7. 영화 상세 정보 + 연관 영화 추천이 완료되면 리뷰 조회와 병렬로 합치기
            return movieDetailMono.zipWith(reviewMono, (movieDetailResult, topReviewResult) -> {
                        // 모든 결과를 종합해서 반환
                        return ResponseDto.response(StatusCode.SUCCESS, movieDetailReviewRecommendResponse);
                    })
                    .onErrorResume(e -> Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 상세 정보 + 연관 영화 + 리뷰 조회결과를 종합해서 반환 중 알 수 없는 오류가 발생: " + e.getMessage())));
        } catch (Exception e) {
            return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "영화 상세 정보 + 연관 영화 + 리뷰 조회 중 알 수 없는 오류가 발생: " + e.getMessage()));
        }
    }


    // TODO: 행동 기반 영화 추천 + 비선호 영화 조회 ( 경로 수정 )
    public Mono<ResponseEntity<ResponseDto>> getActionRecommendationListAsync(int userSeq) {
        // 1. 영화 서버에서 사용자의 최근 행동을 가져옴 (10개)
        String path = util.getUri("/actions/" + userSeq);
        return util.sendGetRequestAsync(movieBaseUrl, path)
                .flatMap(getResponse -> {
                    String userActionResponseDtoJson;
                    try {
                        // getResponse.getBody()가 null인지 확인하고 toString() 처리
                        userActionResponseDtoJson = Objects.requireNonNull(getResponse.getBody()).toString();
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "UserActionResponseDtoJson: getResponse.getBody()가 null입니다." + e.getMessage()));
                    }
                    ResponseDto userActionResponseDto;
                    try {
                        userActionResponseDto = objectMapper.readValue(userActionResponseDtoJson, ResponseDto.class);
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
                                String movieSeqListDtoJson;
                                try {
                                    // movieSeqListDtoJson.getBody()가 null인지 확인하고 toString() 처리
                                    movieSeqListDtoJson = Objects.requireNonNull(postResponse.getBody()).toString();
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieSeqListDtoJson: postResponse.getBody()가 null입니다." + e.getMessage()));
                                }
                                ResponseDto movieSeqListDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    movieSeqListDto = objectMapper.readValue(movieSeqListDtoJson, ResponseDto.class);
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
                                String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                return util.sendGetRequestAsync(movieBaseUrl, unlikeMoviePath)
                                        .flatMap(unlikeResponse -> {
                                            String unlikeResponseDtoJson;
                                            try {
                                                // unlikeResponse.getBody()가 null인지 확인하고 toString() 처리
                                                unlikeResponseDtoJson = Objects.requireNonNull(unlikeResponse.getBody()).toString();
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "UnlikeResponseDtoJson: unlikeResponse.getBody()가 null입니다." + e.getMessage()));
                                            }
                                            ResponseDto unlikeResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                unlikeResponseDto = objectMapper.readValue(unlikeResponseDtoJson, ResponseDto.class);
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
                                                        String movieListResponseDtoJson;
                                                        try {
                                                            // movieListResponse.getBody()가 null인지 확인하고 toString() 처리
                                                            movieListResponseDtoJson = Objects.requireNonNull(movieListResponse.getBody()).toString();
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieListResponseDtoJson: movieListResponse.getBody()가 null입니다." + e.getMessage()));
                                                        }
                                                        ResponseDto movieListResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            movieListResponseDto = objectMapper.readValue(movieListResponseDtoJson, ResponseDto.class);
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


    // TODO: 사용자 평점-리뷰 기반 영화 추천 ( 경로 수정 )
    public Mono<ResponseEntity<ResponseDto>> getReviewRecommendationList(int userSeq) {
        // 1. 유저-리뷰 서버에서 사용자의 리뷰 목록을 가져옴
        String userReviewPath = util.getUri("/list/review/recommendation/" + userSeq);
        return util.sendGetRequestAsync(userBaseUrl, userReviewPath)
                .flatMap(reviewResponse -> {
                    String reviewResponseDtoJson;
                    try {
                        // reviewResponse.getBody()가 null인지 확인하고 toString() 처리
                        reviewResponseDtoJson = Objects.requireNonNull(reviewResponse.getBody()).toString();
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "ReviewResponseDtoJson: reviewResponse.getBody()가 null입니다." + e.getMessage()));
                    }
                    ResponseDto reviewResponseDto;
                    try {
                        // JSON 데이터를 ResponseDto로 역직렬화
                        reviewResponseDto = objectMapper.readValue(reviewResponseDtoJson, ResponseDto.class);
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 리뷰 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 상태 코드가 성공이 아닌 경우 처리
                    if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                                reviewResponseDto.getData()
                        ));
                    }
                    List<RecommendReviewResponse> recommendReviews;
                    try {
                        // reviewResponseDto의 데이터 필드를 List<RecommendReviewResponse>로 변환
                        recommendReviews = objectMapper.convertValue(reviewResponseDto.getData(), new TypeReference<List<RecommendReviewResponse>>() {});
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 리뷰 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // 사용자의 리뷰가 없을 경우 처리
                    if (recommendReviews == null || recommendReviews.isEmpty()) {
                        return getLikeRecommendationList(userSeq);
                    }
                    // 2. 추천 서버로 사용자의 리뷰 목록을 전송하고, Top-N 사용자 리스트를 가져옴
                    String recommendationPath = util.getUri("/list/recommendation/rating");
                    return util.sendPostRequestAsync(recommendBaseUrl, recommendationPath, recommendReviews)
                            .flatMap(toNResponse -> {
                                String toNResponseDtoJson;
                                try {
                                    // toNResponse.getBody()가 null인지 확인하고 toString() 처리
                                    toNResponseDtoJson = Objects.requireNonNull(toNResponse.getBody()).toString();
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "ToNResponseDtoJson: toNResponse.getBody()가 null입니다." + e.getMessage()));
                                }
                                ResponseDto toNResponseDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    toNResponseDto = objectMapper.readValue(toNResponseDtoJson, ResponseDto.class);
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Top-N 사용자 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (toNResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(toNResponseDto.getHttpStatus(), toNResponseDto.getServiceStatus(), toNResponseDto.getMessage()),
                                            toNResponseDto.getData()
                                    ));
                                }
                                List<Integer> topNUserSeqs;
                                try {
                                    // toNResponseDto의 데이터 필드를 List<Integer>로 변환
                                    topNUserSeqs = objectMapper.convertValue(toNResponseDto.getData(), new TypeReference<List<Integer>>() {});
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Top-N 사용자 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // Top-N 사용자가 없을 경우 처리
                                if (topNUserSeqs == null || topNUserSeqs.isEmpty()) {
                                    return getLikeRecommendationList(userSeq);
                                }
                                // 3. 유저-리뷰 서버에서 Top-N 사용자의 리뷰 감성 점수 목록을 가져옴
                                String userReviewListPath = util.getUri("/list/review/sentiment");
                                return util.sendPostRequestAsync(userBaseUrl, userReviewListPath, topNUserSeqs)
                                        .flatMap(sentimentsResponse -> {
                                            String sentimentsResponseDtoJson;
                                            try {
                                                // sentimentsResponse.getBody()가 null인지 확인하고 toString() 처리
                                                sentimentsResponseDtoJson = Objects.requireNonNull(sentimentsResponse.getBody()).toString();
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "SentimentsResponseDtoJson: sentimentsResponse.getBody()가 null입니다." + e.getMessage()));
                                            }
                                            ResponseDto sentimentsResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                sentimentsResponseDto = objectMapper.readValue(sentimentsResponseDtoJson, ResponseDto.class);
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Top-N 사용자의 리뷰 감성 점수 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (sentimentsResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(sentimentsResponseDto.getHttpStatus(), sentimentsResponseDto.getServiceStatus(), sentimentsResponseDto.getMessage()),
                                                        sentimentsResponseDto.getData()
                                                ));
                                            }
                                            List<RecommendSentimentReviewResponse> sentimentReviews;
                                            try {
                                                // sentimentsResponseDto의 데이터 필드를 List<RecommendSentimentReviewResponse>로 변환
                                                sentimentReviews = objectMapper.convertValue(sentimentsResponseDto.getData(), new TypeReference<List<RecommendSentimentReviewResponse>>() {});
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "Top-N 사용자의 리뷰 감성 점수 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 추천 서버로 사용자의 리뷰 목록과 Top-N 사용자의 리뷰 감성 점수 목록을 전송하고, 추천 영화 목록을 가져옴
                                            String recommendationListPath = util.getUri("/list/recommendation/review");
                                            RecommendByReviewRequest recommendByReviewRequest = new RecommendByReviewRequest(userSeq, recommendReviews, sentimentReviews);
                                            return util.sendPostRequestAsync(recommendBaseUrl, recommendationListPath, recommendByReviewRequest)
                                                    .flatMap(recommendResponse -> {
                                                        String recommendResponseDtoJson;
                                                        try {
                                                            // recommendResponse.getBody()가 null인지 확인하고 toString() 처리
                                                            recommendResponseDtoJson = Objects.requireNonNull(recommendResponse.getBody()).toString();
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "RecommendResponseDtoJson: recommendResponse.getBody()가 null입니다." + e.getMessage()));
                                                        }
                                                        ResponseDto recommendResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            recommendResponseDto = objectMapper.readValue(recommendResponseDtoJson, ResponseDto.class);
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 상태 코드가 성공이 아닌 경우 처리
                                                        if (recommendResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(recommendResponseDto.getHttpStatus(), recommendResponseDto.getServiceStatus(), recommendResponseDto.getMessage()),
                                                                    recommendResponseDto.getData()
                                                            ));
                                                        }
                                                        List<Integer> movieSeqList;
                                                        try {
                                                            // recommendResponseDto의 데이터 필드를 List<MovieListResponse>로 변환
                                                            movieSeqList = objectMapper.convertValue(recommendResponseDto.getData(), new TypeReference<List<Integer>>() {});
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                        }
                                                        // 5. 비선호 영화 조회
                                                        String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                                        return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                                                .flatMap(unlikeResponse -> {
                                                                    String unlikeResponseDtoJson;
                                                                    try {
                                                                        // unlikeResponse.getBody()가 null인지 확인하고 toString() 처리
                                                                        unlikeResponseDtoJson = Objects.requireNonNull(unlikeResponse.getBody()).toString();
                                                                    } catch (Exception e) {
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "UnlikeResponseDtoJson: unlikeResponse.getBody()가 null입니다."));
                                                                    }
                                                                    ResponseDto unlikeResponseDto;
                                                                    try {
                                                                        // JSON 데이터를 ResponseDto로 역직렬화
                                                                        unlikeResponseDto = objectMapper.readValue(unlikeResponseDtoJson, ResponseDto.class);
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
                                                                    // 6. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                                    RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                                                    String movieListPath = util.getUri("/list/recommendation");
                                                                    return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
                                                                            .flatMap(movieListResponse -> {
                                                                                String movieListResponseDtoJson;
                                                                                try {
                                                                                    // movieListResponse.getBody()가 null인지 확인하고 toString() 처리
                                                                                    movieListResponseDtoJson = Objects.requireNonNull(movieListResponse.getBody()).toString();
                                                                                } catch (Exception e) {
                                                                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieListResponseDtoJson: movieListResponse.getBody()가 null입니다." + e.getMessage()));
                                                                                }
                                                                                ResponseDto movieListResponseDto;
                                                                                try {
                                                                                    // JSON 데이터를 ResponseDto로 역직렬화
                                                                                    movieListResponseDto = objectMapper.readValue(movieListResponseDtoJson, ResponseDto.class);
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
                                                                                    movieListResponses = objectMapper.convertValue(movieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {});
                                                                                } catch (Exception e) {
                                                                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                                                }
                                                                                // 변환된 목록을 반환
                                                                                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieListResponses));
                                                                            });
                                                                });
                                                    });
                                        });
                            });

                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "리뷰 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }


    public Mono<ResponseEntity<ResponseDto>> getTopMovieList() {
        // 1. 외부 API 경로 설정
        String path = util.getUri("/list/top10");
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }


    // TODO: 선호 기반 영화 추천 ( 경로 수정 )
    private Mono<ResponseEntity<ResponseDto>> getLikeRecommendationList(int userSeq) {
        // 1. 사용자 선호 영화 목록 조회
        String likeMoviePath = util.getUri("/list/like/" + userSeq);
        return util.sendGetRequestAsync(userBaseUrl, likeMoviePath)
                .flatMap(likeResponse -> {
                    String likeResponseDtoJson;
                    try {
                        // likeResponse.getBody()가 null인지 확인하고 toString() 처리
                        likeResponseDtoJson = Objects.requireNonNull(likeResponse.getBody()).toString();
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "LikeResponseDtoJson: likeResponse.getBody()가 null입니다." + e.getMessage()));
                    }
                    ResponseDto likeResponseDto;
                    try {
                        // JSON 데이터를 ResponseDto로 역직렬화
                        likeResponseDto = objectMapper.readValue(likeResponseDtoJson, ResponseDto.class);
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 선호 영화 번호 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
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
                        likeMovieSeqs = objectMapper.convertValue(likeResponseDto.getData(), new TypeReference<List<Integer>>() {});
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 선호 영화 번호 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }

                    // 2. 영화서버에서 선호 영화 목록의 제목리스트를 조회
                    String likeMovieListPath = util.getUri("/list/movieId");
                    return util.sendPostRequestAsync(movieBaseUrl, likeMovieListPath, likeMovieSeqs)
                            .flatMap(likeMovieListResponse -> {
                                String likeMovieListResponseDtoJson;
                                try {
                                    // movieListResponse.getBody()가 null인지 확인하고 toString() 처리
                                    likeMovieListResponseDtoJson = Objects.requireNonNull(likeMovieListResponse.getBody()).toString();
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieListResponseDtoJson: movieListResponse.getBody()가 null입니다." + e.getMessage()));
                                }
                                ResponseDto likeMovieListResponseDto;
                                try {
                                    // JSON 데이터를 ResponseDto로 역직렬화
                                    likeMovieListResponseDto = objectMapper.readValue(likeMovieListResponseDtoJson, ResponseDto.class);
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 선호 영화 제목 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 상태 코드가 성공이 아닌 경우 처리
                                if (likeMovieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(likeMovieListResponseDto.getHttpStatus(), likeMovieListResponseDto.getServiceStatus(), likeMovieListResponseDto.getMessage()),
                                            likeMovieListResponseDto.getData()
                                    ));
                                }
                                List<MovieListResponse> likeMovieTitles;
                                try {
                                    likeMovieTitles = objectMapper.convertValue(likeMovieListResponseDto.getData(), new TypeReference<List<MovieListResponse>>() {});
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "사용자 선호 영화 제목 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // 3. 추천 서버로 사용자 선호 영화 제목들을 전송
                                String recommendationPath = util.getUri("/list/recommendation/like");
                                return util.sendPostRequestAsync(recommendBaseUrl, recommendationPath, likeMovieTitles)
                                        .flatMap(recommendResponse -> {
                                            String recommendResponseDtoJson;
                                            try {
                                                // recommendResponse.getBody()가 null인지 확인하고 toString() 처리
                                                recommendResponseDtoJson = Objects.requireNonNull(recommendResponse.getBody()).toString();
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "RecommendResponseDtoJson: recommendResponse.getBody()가 null입니다." + e.getMessage()));
                                            }
                                            ResponseDto recommendResponseDto;
                                            try {
                                                // JSON 데이터를 ResponseDto로 역직렬화
                                                recommendResponseDto = objectMapper.readValue(recommendResponseDtoJson, ResponseDto.class);
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 상태 코드가 성공이 아닌 경우 처리
                                            if (recommendResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(recommendResponseDto.getHttpStatus(), recommendResponseDto.getServiceStatus(), recommendResponseDto.getMessage()),
                                                        recommendResponseDto.getData()
                                                ));
                                            }
                                            List<Integer> movieSeqList;
                                            try {
                                                // recommendResponseDto의 데이터 필드를 List<Integer>로 변환
                                                movieSeqList = objectMapper.convertValue(recommendResponseDto.getData(), new TypeReference<List<Integer>>() {});
                                            } catch (Exception e) {
                                                return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                            }
                                            // 4. 사용자 비선호 영화 목록 조회
                                            String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                            return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                                    .flatMap(unlikeResponse -> {
                                                        String unlikeResponseDtoJson;
                                                        try {
                                                            // unlikeResponse.getBody()가 null인지 확인하고 toString() 처리
                                                            unlikeResponseDtoJson = Objects.requireNonNull(unlikeResponse.getBody()).toString();
                                                        } catch (Exception e) {
                                                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "UnlikeResponseDtoJson: unlikeResponse.getBody()가 null입니다." + e.getMessage()));
                                                        }
                                                        ResponseDto unlikeResponseDto;
                                                        try {
                                                            // JSON 데이터를 ResponseDto로 역직렬화
                                                            unlikeResponseDto = objectMapper.readValue(unlikeResponseDtoJson, ResponseDto.class);
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
                                                        // 5. 영화 서버에서 영화 목록 조회 후 반환
                                                        RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                                        String movieListPath = util.getUri("/list/recommendation");
                                                        return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
                                                                .flatMap(movieListResponse -> {
                                                                    String movieListResponseDtoJson;
                                                                    try {
                                                                        // movieListResponse.getBody()가 null인지 확인하고 toString() 처리
                                                                        movieListResponseDtoJson = Objects.requireNonNull(movieListResponse.getBody()).toString();
                                                                    } catch (Exception e) {
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "MovieListResponseDtoJson: movieListResponse.getBody()가 null입니다." + e.getMessage()));
                                                                    }
                                                                    ResponseDto movieListResponseDto;
                                                                    try {
                                                                        // JSON 데이터를 ResponseDto로 역직렬화
                                                                        movieListResponseDto = objectMapper.readValue(movieListResponseDtoJson, ResponseDto.class);
                                                                    } catch (Exception e) {
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 기반 추천 영화 목록 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
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
                                                                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "선호 영화 기반 추천 영화 목록 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                                                    }
                                                                    // 변환된 목록을 반환
                                                                    return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieListResponses));
                                                                });
                                            });
                                });
                    });
        }).onErrorResume(e -> {
            if (e instanceof RestApiException ex) {
                return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
            } else {
                return Mono.just(ResponseDto.response(StatusCode.UNKNOW_ERROR, "선호 영화 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
            }
        });
    }

}
