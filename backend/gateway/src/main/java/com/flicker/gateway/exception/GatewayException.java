package com.flicker.gateway.exception;

import com.flicker.gateway.status.StatusCode;
import lombok.Getter;

@Getter
public class GatewayException extends RuntimeException {

    private final StatusCode status;
    private final Object data;

    public GatewayException(final StatusCode statusCode, final Object data) {
        this.status = statusCode;
        this.data = data;
    }

    public GatewayException(final StatusCode statusCode) {
        this(statusCode, null);
    }


}
