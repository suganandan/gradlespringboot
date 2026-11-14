package com.sugan.gradlespringboot.exception;

import com.sugan.gradlespringboot.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleMethodArgumentNotValid_shouldReturnBadRequestWithValidationErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        
        // Create concrete FieldError instances to simulate validation failures
        FieldError fieldError1 = new FieldError("objectName", "field1", "Error message for field1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Error message for field2");
        List<ObjectError> allErrors = Arrays.asList(fieldError1, fieldError2);

        when(bindingResult.getAllErrors()).thenReturn(allErrors);

        // MethodArgumentNotValidException requires a MethodParameter
        MethodParameter methodParameter = mock(MethodParameter.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        HttpHeaders headers = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST; // This status is overridden by the handler

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex, headers, status, webRequest);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) responseEntity.getBody();
        assertThat(errors).hasSize(2);
        assertThat(errors).containsEntry("field1", "Error message for field1");
        assertThat(errors).containsEntry("field2", "Error message for field2");
    }

    @Test
    void handleGlobalException_shouldReturnInternalServerError() {
        // Given
        Exception exception = new RuntimeException("An unexpected error occurred");
        String apiPath = "/api/v1/resource";
        when(webRequest.getDescription(false)).thenReturn(apiPath);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getApiPath()).isEqualTo(apiPath);
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody().getErrorMessage()).isEqualTo("An unexpected error occurred");
        // Check that the error time is recent (within a reasonable delta)
        assertThat(responseEntity.getBody().getErrorTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void handleStandardException_shouldReturnBadRequest() {
        // Given
        StandardException exception = new StandardException("Invalid input data", "INVALID_INPUT");
        String apiPath = "/api/v1/data";
        when(webRequest.getDescription(false)).thenReturn(apiPath);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleStandardException(exception, webRequest);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getApiPath()).isEqualTo(apiPath);
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getErrorMessage()).isEqualTo("Invalid input data");
        // Check that the error time is recent (within a reasonable delta)
        assertThat(responseEntity.getBody().getErrorTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
