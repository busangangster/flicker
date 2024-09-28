package com.flicker.bff.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.user.MovieReviewReqDto;
import com.flicker.bff.dto.user.UserLoginReqDto;
import com.flicker.bff.dto.user.UserRegisterReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
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
    public Mono<ResponseEntity<ResponseEntity<ResponseDto>>> loginUser(UserLoginReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/login");
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendPostRequestAsyncWithToken(userReviewBaseUrl, path, request);
    }

    // 3. 회원수정(LOW)
    // 4. 회원탈퇴(LOW)

//    public Mono<ResponseEntity<ResponseDto>> deleteUser(Integer ) {
//        // 1. 외부 API의 경로를 설정합니다.
//        String path = util.getUri("/login");
//        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
//        return util.sendPostRequestAsync(userReviewBaseUrl, path, request);
//    }


    // 3. 영화 디테일 페이지의 대표 리뷰 조회
    // 4. 리뷰 등록
    // 5. 리뷰 목록
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(MovieReviewReqDto request) {
        // 1. 외부 API의 경로를 설정합니다.
        String path = util.getUri("/review/movies/"+request.getMovieSeq()+"?userSeq="+request.getUserSeq());
        // 2. POST 요청을 비동기적으로 외부 API에 보냅니다.
        return util.sendGetRequestAsync(userReviewBaseUrl,path);
    }

    // 6. 리뷰 삭제
    // 7. 포토 리뷰 조회
    // 8. 선호 영화 등록
    // 9. 선호 영화 조회
    // 10. 비선호 영화 등록
    // 11. 비선호 영화 삭제
    // 12. 비선호 영화 조회
    // 13. 찜한 영화 등록
    // 14. 찜한 영화 삭제
    // 15. 찜한 영화 조회
}
