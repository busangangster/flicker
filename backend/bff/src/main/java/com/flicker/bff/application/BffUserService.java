package com.flicker.bff.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

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
        return util.sendPostRequestAsyncWithToken(userReviewBaseUrl, path, dto);
    }

    // 5. 리뷰 목록
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(MovieReviewReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+request.getMovieSeq()+"?userSeq="+request.getUserSeq());
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

}
