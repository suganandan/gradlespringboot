package com.sugan.gradlespringboot.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    public static final String DEFAULT_MESSAGE = "Unexpected error";

    private String apiPath;
    private HttpStatus errorCode;
    private String errorMessage;
    private LocalDateTime errorTime;

    public boolean emptyMessage() {
        return errorMessage == null || errorMessage.isEmpty();
    }

    public static String formatTime(LocalDateTime t) {
        String r = t.toString();
        return r;
    }

}
