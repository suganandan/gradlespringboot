package com.sugan.gradlespringboot.controller;

import com.sugan.gradlespringboot.dto.ErrorResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Demo Rest API", description = "Demo API for testing DevSecOps")
public class HelloController {
@GetMapping("/")
@Operation(summary = "Home API", description = "Home API to test the Application")
@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
    public ResponseEntity<String> getWelcome(){
         return ResponseEntity.status(200).body("Welcome to Gradle Spring boot");
    }

    public int addNumbers(int val1, int val2){
    return val1+val2;
    }
}
