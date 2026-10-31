// SONAR-FIX [I-001 java:S106] Added SLF4J logger for proper logging practices.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify logger configuration.
package com.sugan.gradlespringboot.exception;

import com.sugan.gradlespringboot.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// SONAR-FIX [I-001 java:S106] Added SLF4J logger for proper logging practices.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify logger configuration.
import org.slf4j.Logger;
// SONAR-FIX [I-001 java:S106] Added SLF4J logger for proper logging practices.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify logger configuration.
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // SONAR-FIX [I-001 java:S106] Declared a static final logger for the class.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify logger configuration.
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with a logger call for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that logging is correctly configured and messages are appropriate for production.
        // SONAR-FIX [I-007 java:S4507] Replaced printStackTrace with a logger call to avoid leaking debug information in production.
        // HUMAN_REVIEW_REQUIRED [I-007]: Verify that logging is correctly configured and messages are appropriate for production.
        log.error("Unhandled exception: {}", exception.getMessage(), exception);
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(StandardException.class)
    public ResponseEntity<ErrorResponseDto> handleStandardException(StandardException exception,WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    // SONAR-FIX [I-002 java:S1144] Removed the unused private method 'unusedHelper'.
    // HUMAN_REVIEW_REQUIRED [I-002]: Confirm that this method is indeed not used anywhere and its removal does not impact functionality.
    // This removal also resolves issues I-003 (unused parameter), I-004 (catch Throwable), I-005 (empty catch block), I-006 (string comparison), and I-008 (redundant toString) which were all within this method.

}
