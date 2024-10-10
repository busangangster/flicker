package com.flicker.bff.presentation;

import com.flicker.bff.application.BffUserService;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.dto.user.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return userService.registerUser(request);
    }

    // 2. 로그인
    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseDto>> loginUser(@RequestBody UserLoginReqDto request) {
        return userService.loginUser(request);
    }

    // 3. 회원수정(LOW)
    @PutMapping("/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> update(@PathVariable(value = "userSeq")Integer userSeq,@RequestBody UserUpdateDto dto){
        return userService.update(userSeq,dto);
    }

    // 4. 회원탈퇴(LOW)
    @DeleteMapping("/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> delete(@PathVariable(value = "userSeq")Integer userSeq,@RequestBody UserUpdateDto dto){
        return userService.delete(userSeq);
    }
    // 3. 영화 디테일 페이지의 대표 리뷰 조회
    @GetMapping("/review/movies/{movieSeq}/popular-review")
    public Mono<ResponseEntity<ResponseDto>> getPopularMovieReviews(@PathVariable("movieSeq") Integer movieSeq, @RequestParam(value = "userSeq") Integer myUserSeq) {
        MovieReviewReqDto dto = new MovieReviewReqDto();
        dto.setMovieSeq(movieSeq);
        dto.setUserSeq(myUserSeq);
        return userService.getMovieReview(dto);
    }
    // 4. 리뷰 등록
    @PostMapping("/review")
    public Mono<ResponseEntity<ResponseDto>> registerReview(@RequestBody RegisterReviewReqDto dto){
        return userService.registerReview(dto);
    }

    // 5. 리뷰 목록
    @GetMapping("/movies/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> getMovieReview(@PathVariable("movieSeq") Integer movieSeq, @RequestParam(value = "userSeq") Integer myUserSeq, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10")Integer size, @RequestParam(value = "option", defaultValue = "like")String option) {

        MovieReviewReqDto dto = new MovieReviewReqDto();
        dto.setMovieSeq(movieSeq);
        dto.setUserSeq(myUserSeq);
        dto.setPage(page);
        dto.setSize(size);
        dto.setOption(option);

        return userService.getMovieReview(dto);
    }

    @GetMapping("/all/movies/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> getAllMovieReview(@PathVariable("movieSeq") Integer movieSeq, @RequestParam(value = "userSeq") Integer myUserSeq, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "20000")Integer size, @RequestParam(value = "option", defaultValue = "like")String option) {

        MovieReviewReqDto dto = new MovieReviewReqDto();
        dto.setMovieSeq(movieSeq);
        dto.setUserSeq(myUserSeq);
        dto.setPage(page);
        dto.setSize(size);
        dto.setOption(option);

        return userService.getAllMovieReview(dto);
    }



    // 6. 리뷰 삭제
    @DeleteMapping("/review")
    public Mono<ResponseEntity<ResponseDto>> deleteReview(@RequestBody DeleteReviewReqDto dto) {
        return userService.deleteReview(dto);
    }
    // 7. 포토 리뷰 조회
    @GetMapping("/review/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> getUserReview(@PathVariable("userSeq") Integer userSeq, @RequestParam(value = "userSeq") Integer myUserSeq) {
        UserReviewReqDto dto = new UserReviewReqDto();
        dto.setUserSeq(userSeq);
        dto.setMyUserSeq(myUserSeq);
        return userService.getUserReviews(dto);
    }

    // 8. 선호 영화 등록
    @PostMapping("/{userSeq}/favorite-movie")
    public Mono<ResponseEntity<ResponseDto>> registerFavoriteMovie(@PathVariable(value = "userSeq")Integer userSeq, @RequestBody MovieSeqListDto dto){
        return userService.registerFavoriteMovie(userSeq, dto);
    }
    // 9. 선호 영화 조회
    @GetMapping("/{userSeq}/favorite-movie")
    public Mono<ResponseEntity<ResponseDto>> getFavoriteMovie(@PathVariable(value = "userSeq")Integer userSeq){
        return userService.getFavoriteMovie(userSeq);
    }

    // 10. 비선호 영화 등록
    @PostMapping("/{userSeq}/dislike-movie/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> registerUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq){
        return userService.registerUnlikeMovie(userSeq, movieSeq);
    }

    // 11. 비선호 영화 삭제
    @DeleteMapping("/{userSeq}/dislike-movie/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> deleteUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq){
        return userService.deleteUnlikeMovie(userSeq,movieSeq);
    }
    // 12. 비선호 영화 조회
    @GetMapping("/{userSeq}/dislike-movie")
    public Mono<ResponseEntity<ResponseDto>> getUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq){
        return userService.getUnlikeMovie(userSeq);
    }
    // 13. 찜한 영화 등록
    @PostMapping("/{userSeq}/bookmark-movie/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> registerBookmarkMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq) {
        return userService.registerBookmarkMovie(userSeq,movieSeq);
    }
    // 14. 찜한 영화 삭제
    @DeleteMapping("/{userSeq}/bookmark-movie/{movieSeq}")
    public Mono<ResponseEntity<ResponseDto>> deleteBookmarkMovie(@PathVariable Integer userSeq, @PathVariable Integer movieSeq){
        return userService.deleteBookmarkMovie(userSeq,movieSeq);
    }
    // 15. 찜한 영화 조회
    @GetMapping("/{userSeq}/bookmark-movie")
    public Mono<ResponseEntity<ResponseDto>> getBookmarkMovie(@PathVariable(value = "userSeq")Integer userSeq){
        return userService.getBookmarkMovie(userSeq);
    }
    // 16. 리뷰 좋아요 등록
    @PostMapping("/review/likeReview")
    public Mono<ResponseEntity<ResponseDto>> addLikeReview(@RequestBody AddLikeReviewReqDto dto) {
        return userService.addLikeReview(dto);
    }
    // 17. 리뷰 좋아요 삭제
    @DeleteMapping("/review/likeReview")
    public Mono<ResponseEntity<ResponseDto>> removeLikeReview(@RequestParam Integer userSeq, @RequestParam Integer reviewSeq){
        return userService.removeLikeReview(userSeq,reviewSeq);
    }

    // 포토 카드 조회
    @GetMapping("/{userSeq}/photocard")
    public Mono<ResponseEntity<ResponseDto>> getPhotoCard(@PathVariable(value = "userSeq")Integer userSeq){
        return userService.getPhotoCard(userSeq);
    }

    // 리뷰 워드 클라우드
    @GetMapping("/review/movies/{movieSeq}/distribute")
    public Mono<ResponseEntity<ResponseDto>> getMovieReviewRatingDistribute(@PathVariable(value = "movieSeq")Integer movieSeq){
        return userService.getMovieReviewRatingDistribute(movieSeq);
    }

    // 마이페이지 리뷰 수 , 라이크 수
    @GetMapping("/{userSeq}/myPage")
    public Mono<ResponseEntity<ResponseDto>> getMyPage(@PathVariable(value = "userSeq")Integer userSeq){
        return userService.getMyPage(userSeq);
    }

    @GetMapping("/review/check-already-review")
    public Mono<ResponseEntity<ResponseDto>> checkAlreadyReview(@RequestParam Integer userSeq, @RequestParam Integer movieSeq){
        return userService.checkAlreadyReview(userSeq,movieSeq);
    }

    @GetMapping("/refresh")
    public Mono<ResponseEntity<ResponseDto>> refresh(HttpServletRequest request, HttpServletResponse response){

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                }
            }
        }

        return userService.refresh(refresh);
    }

    @GetMapping("/check-first-login/{userSeq}")
    public Mono<ResponseEntity<ResponseDto>> checkFirstLogin(@PathVariable Integer userSeq){
        return userService.checkFirstLogin(userSeq);
    }
}
