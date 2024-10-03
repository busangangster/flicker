package com.flicker.user.user.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.jwt.JWTUtil;
import com.flicker.user.review.application.ReviewService;
import com.flicker.user.review.dto.MyPageReviewCntDto;
import com.flicker.user.review.dto.ReviewDto;
import com.flicker.user.user.application.UserService;
import com.flicker.user.user.dto.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JWTUtil jwtUtil;
    private final ReviewService reviewService;
    private final UserService userService;


    // 회원 가입 (아이디 중복 체크)
    // @TODO 아이디 중복 체크, 실제 서비스 실행
    @PostMapping()
    public ResponseEntity<ResponseDto> register(@RequestBody UserRegisterDto dto){

        if(!dto.getEmail().contains("@")){
            throw new RestApiException(StatusCode.INVALID_INPUT_DATA_TYPE,"잘못된 이메일 형식입니다.");
        }
        if(!dto.getPassword().equals(dto.getPassCheck())){
            throw new RestApiException(StatusCode.INVALID_INPUT_DATA_TYPE,"비밀번호가 일치하지 않습니다.");
        }

        boolean register = userService.register(dto);

        if(!register){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "회원가입 완료");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserLoginReqDto dto){

        System.out.println("dto = " + dto);

        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userSeq}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(value = "userSeq")Integer userSeq){

        boolean delete = userService.delete(userSeq);
        if(!delete){
            throw new RestApiException(StatusCode.BAD_REQUEST);
        }
        return ResponseDto.response(StatusCode.SUCCESS, null);
    }

    @PutMapping("/{userSeq}")
    public ResponseEntity<String> update(@PathVariable(value = "userSeq")Integer userSeq,
                                         @RequestBody UserUpdateDto dto, HttpServletResponse response){
        if(userSeq == null || dto.getNickname() == null || dto.getPassword() == null || dto.getEmail() == null){
            throw new RestApiException(StatusCode.BAD_REQUEST);
        }


        UserLoginResDto update = userService.update(userSeq, dto);
        if(update == null){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        String access = jwtUtil.createToken("access", update, "ROLE_USER", 600000L);
        String refresh = jwtUtil.createToken("refresh", update, "ROLE_USER", 86400000L);

        Cookie refreshTokenCookie = new Cookie("refresh", refresh);
        refreshTokenCookie.setHttpOnly(true); // HttpOnly 옵션을 설정하여 자바스크립트에서 접근하지 못하도록
        refreshTokenCookie.setSecure(true);   // HTTPS로만 전송 (개발 환경에서는 false로 설정 가능)
        refreshTokenCookie.setPath("/");      // 쿠키의 경로 설정
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // Refresh 토큰의 유효기간을 설정 (예: 7일)

        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가

        // 6. 새로운 Access 토큰과 함께 응답 반환
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + access)  // Access 토큰을 헤더에 담기
                .body("OK");
    }

    // 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(){
        // TODO 로그아웃은 프론트에서 처리 로그 찍기
        return ResponseDto.response(StatusCode.SUCCESS, null);
    }

    // 관심 영화 추가
    @PostMapping("/{userSeq}/favorite-movie")
    public ResponseEntity<ResponseDto> registerFavoriteMovie(@PathVariable(value = "userSeq")Integer userSeq, @RequestBody MovieSeqListDto dto){

        if(userSeq == null || dto.getMovieSeqList() == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        boolean result = userService.registerFavoriteMovie(userSeq, dto);

        if(!result){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "관심 영화 등록이 완료되었습니다.");
    }

    // 관심없는 영화 추가
    @PostMapping("/{userSeq}/unlike-movie/{movieSeq}")
    public ResponseEntity<ResponseDto> registerUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq){

        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!userService.registerUnlikeMovie(userSeq, movieSeq)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }

    // 찜한 영화 추가
    @PostMapping("/{userSeq}/bookmark-movie/{movieSeq}")
    public ResponseEntity<ResponseDto> registerBookmarkMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq) {
        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        if(!userService.registerBookmarkMovie(userSeq, movieSeq)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }


    // 관심 영화 목록
    @GetMapping("/{userSeq}/favorite-movie")
    public ResponseEntity<ResponseDto> getFavoriteMovie(@PathVariable(value = "userSeq")Integer userSeq){
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        MovieSeqListDto favoriteMovies = userService.getFavoriteMovies(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, favoriteMovies);
    }

    // 관심없는 영화 목록
    @GetMapping("/{userSeq}/unlike-movie")
    public ResponseEntity<ResponseDto> getUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq){
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        MovieSeqListDto favoriteMovies = userService.getUnlikeMovies(userSeq);
        List<Integer> movieSeqList = favoriteMovies.getMovieSeqList();

        return ResponseDto.response(StatusCode.SUCCESS,movieSeqList );
    }

    // 찜한 영화 목록
    @GetMapping("/{userSeq}/bookmark-movie")
    public ResponseEntity<ResponseDto> getBookmarkMovie(@PathVariable(value = "userSeq")Integer userSeq){
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        MovieSeqListDto favoriteMovies = userService.getBookmarkMovies(userSeq);
        List<Integer> movieSeqList = favoriteMovies.getMovieSeqList();
        return ResponseDto.response(StatusCode.SUCCESS, movieSeqList);
    }

    // 관심없는 영화 삭제
    @DeleteMapping("/{userSeq}/unlike-movie/{movieSeq}")
    public ResponseEntity<ResponseDto> deleteUnlikeMovie(@PathVariable(value = "userSeq")Integer userSeq, @PathVariable(value = "movieSeq")Integer movieSeq){
        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        if(!userService.deleteUnlikeMovie(userSeq, movieSeq)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }
    // 찜한 영화 해제
    @DeleteMapping("/{userSeq}/bookmark-movie/{movieSeq}")
    public ResponseEntity<ResponseDto> deleteBookmarkMovie(@PathVariable Integer userSeq, @PathVariable Integer movieSeq){
        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        if(!userService.deleteBookmarkMovie(userSeq, movieSeq)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }

    // 영화 디테일 페이지에 필요한 내용 전달

    // 찜한 영화인지, 비선호영화인지, 비선호영화 목록, 대표 리뷰3건
    @GetMapping("/movie-detail")
    public ResponseEntity<ResponseDto> getMovieDetail(@RequestParam(value = "userSeq")Integer userSeq, @RequestParam(value = "movieSeq")Integer movieSeq){
        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        MovieDetail movieDetail = userService.getMovieDetail(userSeq, movieSeq);

        List<ReviewDto> popularMovieReviews = reviewService.getPopularMovieReviews(movieSeq, userSeq);
        movieDetail.setReviews(popularMovieReviews);

        return ResponseDto.response(StatusCode.SUCCESS, movieDetail);
    }

    @GetMapping("/{userSeq}/myPage")
    public ResponseEntity<ResponseDto> getMyPageMovie(@PathVariable(value = "userSeq")Integer userSeq){
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }

        MyPageReviewCntDto myPageReviewCnt = reviewService.getMyPageReviewCnt(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, myPageReviewCnt);
    }



}
