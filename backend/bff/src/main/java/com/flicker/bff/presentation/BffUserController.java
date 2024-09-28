package com.flicker.bff.presentation;

import com.flicker.bff.application.BffUserService;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.user.MovieReviewReqDto;
import com.flicker.bff.dto.user.UserLoginReqDto;
import com.flicker.bff.dto.user.UserRegisterReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/user")
@RequiredArgsConstructor
public class BffUserController {

    private final BffUserService userService;

    // 1. 회원가입
    @PostMapping()
    public Mono<ResponseEntity<ResponseDto>> registerUser(@RequestBody UserRegisterReqDto request) {
//        return userService.(request);
        System.out.println("회원가입 요청");
        return userService.registerUser(request);
    }

    // 2. 로그인
//    @PostMapping("/login")
//    public Mono<ResponseEntity<ResponseDto>> loginUser(@RequestBody UserLoginReqDto request) {
////        return userService.(request);
//        System.out.println("로그인 요청");
//        return userService.loginUser(request);
//    }
    // 로그인 처리
    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseDto>> loginUser(@RequestBody UserLoginReqDto request, ServerHttpResponse response) {
        return userService.loginUser(request,response);
    }

    // 3. 회원수정(LOW)
    // 4. 회원탈퇴(LOW)
    // 3. 영화 디테일 페이지의 대표 리뷰 조회
    // 4. 리뷰 등록
    // 5. 리뷰 목록
    @GetMapping("/movies/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(@PathVariable("movieSeq") Integer movieSeq, @RequestParam(value = "userSeq") Integer myUserSeq) {
        System.out.println("리뷰 목록 요청");
        System.out.println("movieSeq = " + movieSeq);
        System.out.println("myUserSeq = " + myUserSeq);
        MovieReviewReqDto dto = new MovieReviewReqDto();
        dto.setMovieSeq(movieSeq);
        dto.setUserSeq(myUserSeq);
        return userService.getMovieReview(dto);
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
