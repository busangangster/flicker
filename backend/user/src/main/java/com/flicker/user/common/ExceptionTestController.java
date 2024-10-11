package com.flicker.user.common;


import com.flicker.user.common.exception.RestApiException;
import com.flicker.user.common.response.ResponseDto;
import com.flicker.user.common.status.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionTestController {

    // 정상 처리 시
    @GetMapping("/ok")
    public ResponseEntity<ResponseDto> ok(){
        String data = "전송할 아무 객체";
        return ResponseDto.response(StatusCode.SUCCESS, data);
    }

    // 로그인 안된 경우 예시
    @GetMapping("/login")
    public ResponseEntity<ResponseDto> login(){
        String data = "전송할 아무 객체";
        return ResponseDto.response(StatusCode.INVALID_ID_OR_PASSWORD, data);
    }
    

    // 예외 발생 예시
    @GetMapping("/error")
    public ResponseEntity<ResponseDto> error(){
        throw new RestApiException(StatusCode.BAD_REQUEST, "오류입니다");
    }

}
