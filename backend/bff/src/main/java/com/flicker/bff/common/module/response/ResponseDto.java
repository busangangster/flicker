package com.flicker.bff.common.module.response;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flicker.bff.common.module.status.StatusCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class ResponseDto {

    private final Object data;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int httpStatus;
    private final int serviceStatus;

    // 생성자 기반으로 역직렬화 가능하도록 @JsonCreator와 @JsonProperty 유지
    @JsonCreator
    public ResponseDto(
            @JsonProperty("data") Object data,
            @JsonProperty("message") String message,
            @JsonProperty("httpStatus") int httpStatus,
            @JsonProperty("serviceStatus") int serviceStatus
    ) {
        this.data = data;
        this.message = message;
        this.httpStatus = httpStatus;
        this.serviceStatus = serviceStatus;
    }

    // StatusCode를 처리하는 생성자
    public ResponseDto(StatusCode statusCode, Object data) {
        this(data, statusCode.getMessage(), statusCode.getHttpStatus().value(), statusCode.getServiceStatus());
    }

    public static ResponseEntity<ResponseDto> response(StatusCode statusCode, Object data) {
        return ResponseEntity
                .status(statusCode.getHttpStatus())
                .body(new ResponseDto(statusCode, data));
    }
}
