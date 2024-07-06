package com.sugan.gradlespringboot.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StandardException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;
    private String errorCode;

}

