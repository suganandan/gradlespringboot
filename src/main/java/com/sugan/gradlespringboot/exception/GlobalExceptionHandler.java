// SONAR-FIX [I-007 java:S4507] Replaced printStackTrace with a logger call to avoid debug features in production.
// HUMAN_REVIEW_REQUIRED [I-007]: Verify that logging the exception with a logger is an acceptable replacement for printStackTrace in a production environment.
// SONAR-FIX [I-001 java:S106] Replaced System.out.println with a logger call for proper logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that the logger configuration is appropriate for capturing unhandled exceptions.
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
// SONAR-FIX [I-007 java:S4507] Added SLF4J logger for proper logging instead of System.out or printStackTrace.
// HUMAN_REVIEW_REQUIRED [I-007]: Ensure the logging framework (e.g., Logback, Log4j2) is correctly configured for this logger.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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
        // SONAR-FIX [I-007 java:S4507] Replaced printStackTrace with a logger call to avoid debug features in production.
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with a logger call for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-007]: Verify that logging the exception with a logger is an acceptable replacement for printStackTrace in a production environment.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the logger configuration is appropriate for capturing unhandled exceptions.
        logger.error("Unhandled exception: {}", exception.getMessage(), exception);
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
    // SONAR-FIX [I-003 java:S1172] Removed the unused method parameter 'unusedFlag' by deleting the unused method.
    // SONAR-FIX [I-004 java:S1181] Removed the catch of Throwable by deleting the unused method.
    // SONAR-FIX [I-005 java:S108] Removed the empty catch block by deleting the unused method.
    // SONAR-FIX [I-006 java:S4973] Removed the string comparison with == by deleting the unused method.
    // SONAR-FIX [I-008 java:S1858] Removed the redundant toString() call by deleting the unused method.
    // HUMAN_REVIEW_REQUIRED [I-002]: Confirm that 'unusedHelper' is indeed not called anywhere in the application.
    // HUMAN_REVIEW_REQUIRED [I-003]: Confirm that 'unusedFlag' parameter is not needed by any external callers (though it was private).
    // HUMAN_REVIEW_REQUIRED [I-004]: Confirm that removing the catch block for Throwable does not introduce new issues.
    // HUMAN_REVIEW_REQUIRED [I-005]: Confirm that removing the empty catch block does not hide any intended behavior.
    // HUMAN_REVIEW_REQUIRED [I-006]: Confirm that removing the string comparison with == does not affect any logic.
    // HUMAN_REVIEW_REQUIRED [I-008]: Confirm that removing the redundant toString() call does not affect any logic.

}
