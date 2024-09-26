package com.flicker.bff.application;

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


@Service
@RequiredArgsConstructor
public class BffMovieService {

    private final Util util; // Util 클래스 의존성 주입

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
                        ResponseDto movieDetailResponseDto = getResponse.getBody();
                        // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                        if (movieDetailResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                            return Mono.error(new RestApiException(
                                    StatusCode.of(movieDetailResponseDto.getHttpStatus(), movieDetailResponseDto.getServiceStatus(), movieDetailResponseDto.getMessage()),
                                    movieDetailResponseDto.getData()
                            ));
                        }
                        MovieDetailResponse movieDetailResponse = (MovieDetailResponse) movieDetailResponseDto.getData();
                        movieDetailReviewRecommendResponse.setMovieDetail(movieDetailResponse);

                        // 2. 연관 영화 추천 가져오기
                        String recommendationPath = util.getUri("/list/recommendation/similar/" + movieDetailResponse.getMovieTitle());
                        return util.sendGetRequestAsync(recommendBaseUrl, recommendationPath)
                                .flatMap(recommendResponse -> {
                                    // 3. 추천 영화 목록을 가져와서 영화 서버에 요청
                                    ResponseDto movieSeqListResponseDto = recommendResponse.getBody();
                                    if (movieSeqListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                        return Mono.error(new RestApiException(
                                                StatusCode.of(movieSeqListResponseDto.getHttpStatus(), movieSeqListResponseDto.getServiceStatus(), movieSeqListResponseDto.getMessage()),
                                                movieSeqListResponseDto.getData()
                                        ));
                                    }
                                    List<Integer> movieSeqList = (List<Integer>) movieSeqListResponseDto.getData();
                                    String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                    return util.sendGetRequestAsync(movieBaseUrl, unlikeMoviePath)
                                            .flatMap(unlikeResponse -> {
                                                ResponseDto unlikeResponseDto = unlikeResponse.getBody();
                                                if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                    return Mono.error(new RestApiException(
                                                            StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                            unlikeResponseDto.getData()
                                                    ));
                                                }
                                                List<Integer> unlikeMovieSeqs = (List<Integer>) unlikeResponseDto.getData();
                                                RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                                // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                String movieListPath = util.getUri("/list/recommendation");
                                                return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
                                                        .flatMap(movieListResponse -> {
                                                            ResponseDto movieListResponseDto = movieListResponse.getBody();
                                                            if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                                return Mono.error(new RestApiException(
                                                                        StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                        movieListResponseDto.getData()
                                                                ));
                                                            }
                                                            List<MovieListResponse> movieListResponses = (List<MovieListResponse>) movieListResponseDto.getData();
                                                            movieDetailReviewRecommendResponse.setSimilarMovies(movieListResponses);
                                                            return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "성공"));
                                                        });
                                            });
                                });
                    })
                    .onErrorResume(e -> {
                        // 발생한 예외 정보를 클라이언트에 전달
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 서버에서 상세조회, 추천 서버에서 연관영화를 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });

            // 4. 유저-리뷰 서버에서 좋아요 높은 리뷰 조회 (비동기로 병렬 처리)
            String userReviewPath = util.getUri("/list/review/top/" + movieSeq);
            Mono<ResponseEntity<ResponseDto>> reviewMono = util.sendGetRequestAsync(userBaseUrl, userReviewPath)
                    .flatMap(reviewResponse -> {
                        ResponseDto reviewResponseDto = reviewResponse.getBody();
                        if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                            return Mono.error(new RestApiException(
                                    StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                                    reviewResponseDto.getData()
                            ));
                        }
                        List<ReviewResponse> topReviews = (List<ReviewResponse>) reviewResponseDto.getData();
                        movieDetailReviewRecommendResponse.setReviews(topReviews);
                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "성공"));
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "유저-리뷰 서버에서 좋아요 높은 리뷰를 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
            // 5. 영화 상세 정보 + 연관 영화 추천이 완료되면 리뷰 조회와 병렬로 합치기
            return movieDetailMono.zipWith(reviewMono, (movieDetailResult, topReviewResult) -> {
                        // 모든 결과를 종합해서 반환
                        return ResponseDto.response(StatusCode.SUCCESS, movieDetailReviewRecommendResponse);
                    })
                    .onErrorResume(e -> Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 상세 정보 + 연관 영화 + 리뷰 조회결과를 종합해서 반환 중 알 수 없는 오류가 발생: " + e.getMessage())));
        } catch (Exception e) {
            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 상세 정보 + 연관 영화 + 리뷰 조회 중 알 수 없는 오류가 발생: " + e.getMessage()));
        }
    }


    // TODO: 행동 기반 영화 추천 + 비선호 영화 조회 ( 경로 수정 )
    public Mono<ResponseEntity<ResponseDto>> getActionRecommendationListAsync(int userSeq) {
        // 1. 영화 서버에서 사용자의 최근 행동을 가져옴 (10개)
        String path = util.getUri("/actions/" + userSeq);
        return util.sendGetRequestAsync(movieBaseUrl, path)
                .flatMap(getResponse -> {
                    ResponseDto userActionResponseDto = getResponse.getBody();
                    if (userActionResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(userActionResponseDto.getHttpStatus(), userActionResponseDto.getServiceStatus(), userActionResponseDto.getMessage()),
                                userActionResponseDto.getData()
                        ));
                    }
                    List<UserActionResponse> userActions = (List<UserActionResponse>) userActionResponseDto.getData();
                    if (userActions == null || userActions.isEmpty()) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_CONTENT, "사용자의 최근 행동이 없습니다."));
                    }
                    // 2. 추천 서버로 사용자의 최근 행동 목록을 전송하고, 추천 영화 목록을 가져옴
                    String recommendationPath = util.getUri("/list/recommendation/action");
                    return util.sendPostRequestAsync(recommendBaseUrl, recommendationPath, userActions)
                            .flatMap(postResponse -> {
                                List<Integer> movieSeqList = (List<Integer>) postResponse.getBody().getData();
                                // 3. 사용자의 비선호 영화 목록을 가져옴
                                String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                return util.sendGetRequestAsync(movieBaseUrl, unlikeMoviePath)
                                        .flatMap(unlikeResponse -> {
                                            ResponseDto unlikeResponseDto = unlikeResponse.getBody();
                                            if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                        unlikeResponseDto.getData()
                                                ));
                                            }
                                            List<Integer> unlikeMovieSeqs = (List<Integer>) unlikeResponseDto.getData();
                                            RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                            // 4. 추천 영화 목록을 가져와서 영화 서버에 요청
                                            String movieListpath = util.getUri("/list/recommendation");
                                            return util.sendPostRequestAsync(movieBaseUrl, movieListpath, recommendMovieListRequest)
                                                    .flatMap(movieListResponse -> {
                                                        ResponseDto movieListResponseDto = movieListResponse.getBody();
                                                        if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                    movieListResponseDto.getData()
                                                            ));
                                                        }
                                                        List<MovieListResponse> movieListResponses = (List<MovieListResponse>) movieListResponseDto.getData();
                                                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, movieListResponses));
                                                    });
                                        });
                            });
                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 서버에서 행동 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }


    // TODO: 사용자 평점-리뷰 기반 영화 추천
    public Mono<ResponseEntity<ResponseDto>> getReviewRecommendationList(int userSeq) {
        // 1. 유저-리뷰 서버에서 사용자의 리뷰 목록을 가져옴
        String userReviewPath = util.getUri("/list/review/recommendation/" + userSeq);
        return util.sendGetRequestAsync(userBaseUrl, userReviewPath)
                .flatMap(reviewResponse -> {
                    ResponseDto reviewResponseDto = reviewResponse.getBody();
                    if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                                reviewResponseDto.getData()
                        ));
                    }
                    List<RecommendReviewResponse> recommendReviews = (List<RecommendReviewResponse>) reviewResponseDto.getData();
                    if (recommendReviews == null || recommendReviews.isEmpty()) {
                        return Mono.just(ResponseDto.response(StatusCode.NO_CONTENT, "사용자의 리뷰가 없습니다."));
                    }
                    // 2. 추천 서버로 사용자의 리뷰 목록을 전송하고, Top-N 사용자 리스트를 가져옴
                    String recommendationPath = util.getUri("/list/recommendation/rating");
                    return util.sendPostRequestAsync(recommendBaseUrl, recommendationPath, recommendReviews)
                            .flatMap(toNResponse -> {
                                ResponseDto toNResponseDto = toNResponse.getBody();
                                if (toNResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(toNResponseDto.getHttpStatus(), toNResponseDto.getServiceStatus(), toNResponseDto.getMessage()),
                                            toNResponseDto.getData()
                                    ));
                                }
                                List<Integer> topNUserSeqs = (List<Integer>) toNResponseDto.getData();
                                // 3. 유저-리뷰 서버에서 Top-N 사용자의 리뷰 감성 점수 목록을 가져옴
                                String userReviewListPath = util.getUri("/list/review/sentiment");
                                return util.sendPostRequestAsync(userBaseUrl, userReviewListPath, topNUserSeqs)
                                        .flatMap(sentimentsResponse -> {
                                            ResponseDto sentimentsResponseDto = sentimentsResponse.getBody();
                                            if (sentimentsResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                return Mono.error(new RestApiException(
                                                        StatusCode.of(sentimentsResponseDto.getHttpStatus(), sentimentsResponseDto.getServiceStatus(), sentimentsResponseDto.getMessage()),
                                                        sentimentsResponseDto.getData()
                                                ));
                                            }
                                            List<RecommendSentimentReviewResponse> sentimentReviews = (List<RecommendSentimentReviewResponse>) sentimentsResponseDto.getData();

                                            // 4. 추천 서버로 사용자의 리뷰 목록과 Top-N 사용자의 리뷰 감성 점수 목록을 전송하고, 추천 영화 목록을 가져옴
                                            String recommendationListPath = util.getUri("/list/recommendation/review");
                                            RecommendByReviewRequest recommendByReviewRequest = new RecommendByReviewRequest(recommendReviews, sentimentReviews);
                                            return util.sendPostRequestAsync(recommendBaseUrl, recommendationListPath, recommendByReviewRequest)
                                                    .flatMap(recommendResponse -> {
                                                        ResponseDto recommendResponseDto = recommendResponse.getBody();
                                                        if (recommendResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                            return Mono.error(new RestApiException(
                                                                    StatusCode.of(recommendResponseDto.getHttpStatus(), recommendResponseDto.getServiceStatus(), recommendResponseDto.getMessage()),
                                                                    recommendResponseDto.getData()
                                                            ));
                                                        }
                                                        List<Integer> movieSeqList = (List<Integer>) recommendResponseDto.getData();
                                                        // 5. 비선호 영화 조회
                                                        String unlikeMoviePath = util.getUri("/list/unlike/" + userSeq);
                                                        return util.sendGetRequestAsync(userBaseUrl, unlikeMoviePath)
                                                                .flatMap(unlikeResponse -> {
                                                                    ResponseDto unlikeResponseDto = unlikeResponse.getBody();
                                                                    if (unlikeResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                                        return Mono.error(new RestApiException(
                                                                                StatusCode.of(unlikeResponseDto.getHttpStatus(), unlikeResponseDto.getServiceStatus(), unlikeResponseDto.getMessage()),
                                                                                unlikeResponseDto.getData()
                                                                        ));
                                                                    }
                                                                    List<Integer> unlikeMovieSeqs = (List<Integer>) unlikeResponseDto.getData();
                                                                    RecommendMovieListRequest recommendMovieListRequest = new RecommendMovieListRequest(movieSeqList, unlikeMovieSeqs);
                                                                    // 6. 추천 영화 목록을 가져와서 영화 서버에 요청
                                                                    String movieListPath = util.getUri("/list/recommendation");
                                                                    return util.sendPostRequestAsync(movieBaseUrl, movieListPath, recommendMovieListRequest)
                                                                            .flatMap(movieListResponse -> {
                                                                                ResponseDto movieListResponseDto = movieListResponse.getBody();
                                                                                if (movieListResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                                                                    return Mono.error(new RestApiException(
                                                                                            StatusCode.of(movieListResponseDto.getHttpStatus(), movieListResponseDto.getServiceStatus(), movieListResponseDto.getMessage()),
                                                                                            movieListResponseDto.getData()
                                                                                    ));
                                                                                }
                                                                                List<MovieListResponse> movieListResponses = (List<MovieListResponse>) movieListResponseDto.getData();
                                                                                return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "성공"));
                                                                            });
                                                                });
                                                    });
                                        });
                            });

                }).onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 기반 추천을 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });
    }


    public Mono<ResponseEntity<ResponseDto>> getTopMovieList() {
        // 1. 외부 API 경로 설정
        String path = util.getUri("/list/top10");
        // 2. GET 요청 메서드를 사용하여 외부 API에 요청을 보냅니다.
        return util.sendGetRequestAsync(movieBaseUrl, path);
    }
}
