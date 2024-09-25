package com.flicker.user.user.presentation;

import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import com.flicker.user.user.application.UserService;
import com.flicker.user.user.domain.entity.User;
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
        userService.register(dto);
        return ResponseDto.response(StatusCode.SUCCESS, "회원가입 완료");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserLoginReqDto dto, HttpServletRequest request){

//        if(dto.getUserId() == null || dto.getPassword() == null){
//            throw new RestApiException(StatusCode.INVALID_INPUT_DATA_TYPE,"아이디, 패스워드가 공백일 수 없습니다.");
//        }
//        UserLoginResDto loginResDto = userService.login(dto);
        String username = request.getParameter("username");
        System.out.println(username);

        return ResponseDto.response(StatusCode.SUCCESS, "OK");
    }

    // 회원 탈퇴
    @PutMapping()
    public ResponseEntity<ResponseDto> delete(){
        //헤더에서 유저 정보 가져오기
        return ResponseDto.response(StatusCode.SUCCESS, null);
    }

//    @PostMapping("/user/api/join")
//    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
//
//        joinService.joinProcess(joinDto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/user/api/test")
//    public void test(HttpServletRequest request) {
//
//        String username = request.getHeader("X-auth-username");
//        System.out.println(username);
//    }
    // 로그 아웃

    // 선호 영화 추가

    // 선호 영화 삭제

    // 추천 영화 조회 ?

    // 비선호 영화 목록

    // 비선호 영화 삭제

    // 비선호 영화 추가

    // 관심 영화 등록

    // 관심 영화 해제

    // 평점 등록

    // 평점 삭제

    // 전체 평점 및 리뷰 조회

    // 포토카드 조회


}
