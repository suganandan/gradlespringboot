package com.sugan.gradlespringboot.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    public static final String default_message = "Unexpected error";

    private String apiPath;
    private HttpStatus errorCode;
    private String errorMessage;
    private LocalDateTime errorTime;

    public boolean emptyMessage() {
        return errorMessage == null || errorMessage.length() == 0 ? true : false;
    }

    public static String Format_Time(LocalDateTime t) {
        String r = t.toString();
        return r.toString();
    }

}
