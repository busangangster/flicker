package com.flicker.bff.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.MovieDetailResponse;
import com.flicker.bff.dto.user.*;
import com.flicker.bff.dto.user.photocard.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BffUserService {

    private final Util util; // Util 클래스 의존성 주입

    private final ObjectMapper objectMapper;

    @Value("${user-review.baseurl}")
    private String userReviewBaseUrl; // 사용자-리뷰 서버 API의 기본 URL

    @Value("${movie.baseurl}")
    private String movieBaseUrl; // 영화 서버 API의 기본 URL


    // 1. 회원가입
    public Mono<ResponseEntity<ResponseDto>> registerUser(UserRegisterReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsync(userReviewBaseUrl, path, request);
    }

    // 2. 로그인
    public Mono<ResponseEntity<ResponseDto>> loginUser(UserLoginReqDto request) {
        System.out.println("loginService");
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/login");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsyncWithToken(userReviewBaseUrl, path, request);
    }

    // 3. 회원수정(LOW)
    // 4. 회원탈퇴(LOW)
    public Mono<ResponseEntity<ResponseDto>> delete(Integer userSeq) {
        String path = util.getUri("/" + userSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }


    // 3. 영화 디테일 페이지의 대표 리뷰 조회
    public Mono<ResponseEntity<ResponseDto>> getPopularMovieReviews(MovieReviewReqDto dto) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+dto.getMovieSeq()+"/popular-review?userSeq="+dto.getUserSeq());
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    // 4. 리뷰 등록
    public Mono<ResponseEntity<ResponseDto>> registerReview(RegisterReviewReqDto dto) {
        String path = util.getUri("/review");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }

    // 5. 리뷰 목록
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(MovieReviewReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+request.getMovieSeq()+"?userSeq="+request.getUserSeq()+"&page="+request.getPage())+"&size="+request.getSize()+"&option="+request.getOption();
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 6. 리뷰 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteReview(DeleteReviewReqDto dto) {
        String path = util.getUri("/review?reviewSeq="+dto.getReviewSeq()+"&userSeq="+dto.getUserSeq());
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }

    // 7. 포토 리뷰 조회
    public Mono<ResponseEntity<ResponseDto>> getUserReviews(UserReviewReqDto dto){
        String path = util.getUri("/review/"+dto.getUserSeq());
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    // 8. 선호 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerFavoriteMovie(Integer userSeq, MovieSeqListDto dto){
        String path = util.getUri("/"+userSeq+"/favorite-movie");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }

    // 9. 선호 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getFavoriteMovie(Integer userSeq){
        String path = util.getUri("/"+userSeq+"/favorite-movie");
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 10. 비선호 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerUnlikeMovie(Integer userSeq, Integer movieSeq){
        String path = util.getUri("/"+userSeq+"/unlike-movie/"+movieSeq);
        return util.sendPostRequestAsync(userReviewBaseUrl,path,null);
    }
    // 11. 비선호 영화 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteUnlikeMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/unlike-movie/"+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl,path);
    }
    // 12. 비선호 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getUnlikeMovie(Integer userSeq) {
        String path = util.getUri("/"+userSeq+"/unlike-movie");
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 13. 찜한 영화 등록
    public Mono<ResponseEntity<ResponseDto>> registerBookmarkMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie/"+movieSeq);
        return util.sendPostRequestAsync(userReviewBaseUrl, path, null);
    }
    // 14. 찜한 영화 삭제
    public Mono<ResponseEntity<ResponseDto>> deleteBookmarkMovie(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie/"+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }
    // 15. 찜한 영화 조회
    public Mono<ResponseEntity<ResponseDto>> getBookmarkMovie(Integer userSeq) {
        String path = util.getUri("/"+userSeq+"/bookmark-movie");
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }
    // 16. 리뷰 좋아요 등록
    public Mono<ResponseEntity<ResponseDto>> addLikeReview(AddLikeReviewReqDto dto) {
        String path = util.getUri("/review/likeReview");
        return util.sendPostRequestAsync(userReviewBaseUrl, path, dto);
    }
    // 17. 리뷰 좋아요 삭제
    public Mono<ResponseEntity<ResponseDto>> removeLikeReview(Integer userSeq, Integer movieSeq) {
        String path = util.getUri("/review/likeReview?userSeq="+userSeq+"&reviewSeq="+movieSeq);
        return util.sendDeleteRequestAsync(userReviewBaseUrl, path);
    }

    // 18. 포토 카드 조회
    public Mono<ResponseEntity<ResponseDto>> getPhotoCard(Integer userSeq) {

        PhotoCardListDto result = new PhotoCardListDto();

        // 0. 응답 객체 생성
        ReviewListDto dto = new ReviewListDto();

        // 1. 리류 서버에서 자신의 리뷰 가졍기
        UserReviewReqDto reqDto = new UserReviewReqDto(userSeq,userSeq);

        Mono<ResponseEntity<ResponseDto>> photoCard =
                getUserReviews(reqDto).flatMap(getResponse -> {

                    // 응답을 String으로 받는다.
                    String reviewResponseDtoJson;
                    try {
                        reviewResponseDtoJson = Objects.requireNonNull(getResponse.getBody()).toString();
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "reviewResponseDtoJson: getResponse.getBody()가 null입니다." + e.getMessage()));
                    }

                    

                    // String을 responseDto로 변환한다/
                    ResponseDto reviewResponseDto;
                    try {
                        reviewResponseDto = objectMapper.readValue(reviewResponseDtoJson, ResponseDto.class);
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }
                    // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                    if (reviewResponseDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                        return Mono.error(new RestApiException(
                                StatusCode.of(reviewResponseDto.getHttpStatus(), reviewResponseDto.getServiceStatus(), reviewResponseDto.getMessage()),
                                reviewResponseDto.getData()
                        ));
                    }

                    // ObjectMapper를 이용하여 JSON 데이터를 REviewListDto로 역직렬화
                    ReviewListDto reviewListDto;
                    try {
                        reviewListDto = objectMapper.convertValue(reviewResponseDto.getData(), ReviewListDto.class);
                    } catch (Exception e) {
                        return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "리뷰 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                    }

                    List<Integer> movieSeqList = new ArrayList<>();
                    for (ReviewDto item : reviewListDto.getReviews()) {
                        movieSeqList.add(item.getMovieSeq());
                    }






                    String path = util.getUri("/list/movieId");
                    Mono<ResponseEntity<ResponseDto>> responseEntityMono = util.sendPostRequestAsync(movieBaseUrl, path, movieSeqList);
                    responseEntityMono.flatMap(getResponseTwo -> {

                                // 응답을 String으로 받는다.
                                String movieUrlJson;
                                try {
                                    movieUrlJson = Objects.requireNonNull(getResponseTwo.getBody()).toString();
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "movieUrlJson: getResponse.getBody()가 null입니다." + e.getMessage()));
                                }

                                // String을 responseDto로 변환한다/
                                ResponseDto movieDto;
                                try {
                                    movieDto = objectMapper.readValue(movieUrlJson, ResponseDto.class);
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 이미지 Body를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }
                                // ResponseDto의 상태 코드가 성공이 아닌 경우 처리
                                if (movieDto.getServiceStatus() != StatusCode.SUCCESS.getServiceStatus()) {
                                    return Mono.error(new RestApiException(
                                            StatusCode.of(movieDto.getHttpStatus(), movieDto.getServiceStatus(), movieDto.getMessage()),
                                            movieDto.getData()
                                    ));
                                }

                                // ObjectMapper를 이용하여 JSON 데이터를 REviewListDto로 역직렬화
                                MovieImageListDto movieImageListDto;
                                try {
                                    movieImageListDto = objectMapper.convertValue(reviewResponseDto.getData(), MovieImageListDto.class);
                                } catch (Exception e) {
                                    return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "영화 데이터를 역직렬화하는데 오류 발생: " + e.getMessage()));
                                }


                                ArrayList<PhotoCardDto> list = new ArrayList<>();
                                for(int i=0;i<movieImageListDto.getMovieImages().size();i++){
                                    PhotoCardDto photoCardDto = new PhotoCardDto();
                                    photoCardDto.setReviewDto(reviewListDto.getReviews().get(i));
                                    photoCardDto.setMovieImageDto(movieImageListDto.getMovieImages().get(i));
                                }

                        return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "영화 포스터 조회 성공"));
                    });
                    return Mono.just(ResponseDto.response(StatusCode.SUCCESS, "영화 상세 조회 및 연관 영화 조회 성공"));
                })
                .onErrorResume(e -> {
                    if (e instanceof RestApiException ex) {
                        return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                    } else {
                        return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "영화 서버에서 상세조회, 추천 서버에서 연관영화를 가져오는데 알 수 없는 오류 발생: " + e.getMessage()));
                    }
                });

        return photoCard;
    }
}
