package com.sugan.gradlespringboot.exception;

import lombok.*;

import java.io.Serial;

@Setter
@Getter
@AllArgsConstructor
public class StandardException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public static int instanceCount = 0;
    public static final String ERROR_prefix = "ERR-";

    private final String message;
    // SONAR-FIX [I-001 java:S1165] Made the 'errorCode' field final to ensure immutability of the exception state.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify that making 'errorCode' final does not break any existing logic that might have attempted to modify it after construction.
    private final String errorCode;

    public String describe() {
        System.out.println("Describing " + message);
        return message.toString() + "-" + errorCode.toString();
    }

}
