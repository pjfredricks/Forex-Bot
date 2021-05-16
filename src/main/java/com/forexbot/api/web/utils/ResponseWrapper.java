package com.forexbot.api.web.utils;

import lombok.Builder;
import lombok.Data;

import static com.forexbot.api.web.utils.Constants.*;

@Data
@Builder
public class ResponseWrapper {
    private String status;
    private String message;
    private Object data;

    public static ResponseWrapper buildSuccessResponse(String message, Object data) {
        return buildResponse(SUCCESS, message, data);
    }

    public static ResponseWrapper buildErrorResponse(String message, Object data) {
        return buildResponse(ERROR, message, data);
    }

    public static ResponseWrapper buildResponse(String status, String message, Object data) {
        return ResponseWrapper.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
