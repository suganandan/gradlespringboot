// SONAR-FIX [I-001 java:S1611] Removed parentheses around the single lambda parameter 'error' for improved readability.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that the lambda expression still functions as intended after removing the parentheses.
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

// SONAR-FIX [I-001 java:S106] Added import for Logger.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logging framework is configured and used.
import org.slf4j.Logger;
// SONAR-FIX [I-001 java:S106] Added import for LoggerFactory.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logging framework is configured and used.
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // SONAR-FIX [I-001 java:S106] Declared a static logger for the class.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify the logger is correctly initialized and used.
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        // SONAR-FIX [I-007 java:S4507] Replaced printStackTrace with logger.error to avoid exposing debug information in production.
        // HUMAN_REVIEW_REQUIRED [I-007]: Verify that sensitive information is not logged and the logging level is appropriate for production.
        logger.error("An unexpected error occurred", exception);
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with logger.error for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the error message is logged correctly.
        logger.error("Unhandled exception: {}", exception.getMessage());
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

    // SONAR-FIX [I-002 java:S1144] Removed unused private method 'unusedHelper'.
    // SONAR-FIX [I-003 java:S1172] Removed unused method parameter 'unusedFlag' by deleting the unused method.
    // SONAR-FIX [I-004 java:S1181] Removed catch of Throwable by deleting the unused method.
    // SONAR-FIX [I-005 java:S108] Removed empty catch block by deleting the unused method.
    // SONAR-FIX [I-006 java:S4973] Removed string comparison with == by deleting the unused method.
    // SONAR-FIX [I-008 java:S1858] Removed redundant toString() call by deleting the unused method.
    // HUMAN_REVIEW_REQUIRED [I-002, I-003, I-004, I-005, I-006, I-008]: Verify that the 'unusedHelper' method is indeed not used anywhere in the application and its removal does not introduce regressions.
}
