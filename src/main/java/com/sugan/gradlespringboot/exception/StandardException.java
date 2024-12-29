package com.sugan.gradlespringboot.exception;

import lombok.*;

import java.io.Serial;

@Setter
@Getter
@AllArgsConstructor
public class StandardException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
   
    private final String message;
    private String errorCode;


}

