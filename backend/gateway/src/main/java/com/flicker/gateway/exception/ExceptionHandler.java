package com.flicker.gateway.exception;

import com.flicker.gateway.response.ResponseDto;
import com.flicker.gateway.status.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    /**
     * @ExceptionHandler(감지 할 예외)
     * INTERNAL_SERVER_ERROR 예외의 상태 코드와 메시지를 ResponseEntity<ResponseDto> 만들어준다.
     * return handleExceptionInternal(ErrorCode.INTERNAL_SERVER_ERROR);
     */
    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public Mono<ResponseEntity<ResponseDto>> handleAllException(Exception ex) {
        log.error("[exceptionHandle] ex", ex);
        return handleExceptionInternal(StatusCode.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GatewayException.class)
    public Mono<ResponseEntity<ResponseDto>> handleRestApiException(GatewayException ex) {
        return CustomHandleExceptionInternal(ex.getStatus(), ex.getData());
    }

    private Mono<ResponseEntity<ResponseDto>> handleExceptionInternal(StatusCode errorCode) {
        return Mono.just(
                ResponseEntity
                        .status(errorCode.getHttpStatus())
                        .body(new ResponseDto(errorCode, null))
        );
    }

    private Mono<ResponseEntity<ResponseDto>> CustomHandleExceptionInternal(StatusCode errorCode, Object data) {
        return Mono.just(
                ResponseEntity
                        .status(errorCode.getHttpStatus())
                        .body(new ResponseDto(errorCode, data))
        );
    }
}