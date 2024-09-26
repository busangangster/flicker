package com.flicker.user.user.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.application.UserService;
import com.flicker.user.user.domain.entity.User;
import com.flicker.user.user.dto.MovieSeqListDto;
import com.flicker.user.user.dto.UserLoginReqDto;
import com.flicker.user.user.dto.UserRegisterDto;
import com.flicker.user.user.infrastructure.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/auth-test")
    public ResponseEntity<ResponseDto> login(HttpServletRequest request){

        Cookie[] cookies = request.getCookies(); // 요청에서 쿠키 배열 가져오기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
            }
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userId == null) {
            throw new RestApiException(StatusCode.INVALID_ID_OR_PASSWORD);
        }
        User byUserId = userRepository.findByUserId(userId);

        return ResponseDto.response(StatusCode.SUCCESS ,byUserId);
    }

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
    public ResponseEntity<ResponseDto> login(@RequestBody UserLoginReqDto dto, HttpServletRequest request){

        String username = request.getParameter("username");

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
        return ResponseDto.response(StatusCode.SUCCESS, favoriteMovies);
    }

    // 찜한 영화 목록
    @GetMapping("/{userSeq}/bookmark-movie")
    public ResponseEntity<ResponseDto> getBookmarkMovie(@PathVariable(value = "userSeq")Integer userSeq){
        if(userSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        MovieSeqListDto favoriteMovies = userService.getBookmarkMovies(userSeq);
        return ResponseDto.response(StatusCode.SUCCESS, favoriteMovies);
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
    @DeleteMapping("/{userId}/bookmark-movie/{movieSeq}")
    public ResponseEntity<ResponseDto> deleteBookmarkMovie(@PathVariable Integer userSeq, @PathVariable Integer movieSeq){
        if(userSeq == null || movieSeq == null){
            throw new RestApiException(StatusCode.VALUE_CANT_NULL);
        }
        if(!userService.deleteBookmarkMovie(userSeq, movieSeq)){
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }

    // 추천 영화 조회 ?

    // 평점 등록

    // 평점 삭제

    // 전체 평점 및 리뷰 조회

    // 포토카드 조회


}
