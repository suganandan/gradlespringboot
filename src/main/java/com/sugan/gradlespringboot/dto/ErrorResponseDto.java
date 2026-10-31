package com.sugan.gradlespringboot.dto;

import java.time.LocalDateTime;

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

    // SONAR-FIX [I-002 java:S1125] Removed unnecessary boolean literals from the ternary operator.
    // SONAR-FIX [I-003 java:S7158] Used isEmpty() to check if the string is empty for better readability and performance.
    // HUMAN_REVIEW_REQUIRED [I-002]: Verify the boolean logic remains correct after removing the ternary operator.
    // HUMAN_REVIEW_REQUIRED [I-003]: Verify the string emptiness check is correct using isEmpty().
    public boolean emptyMessage() {
        return errorMessage == null || errorMessage.isEmpty();
    }

    public static String Format_Time(LocalDateTime t) {
        String r = t.toString();
        // SONAR-FIX [I-004 java:S1858] Removed redundant toString() call on a String object.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify that returning the string directly is the intended behavior.
        return r;
    }

}
