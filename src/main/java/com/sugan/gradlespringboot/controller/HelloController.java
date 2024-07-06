package com.sugan.gradlespringboot.controller;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.dto.ErrorResponseDto;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.exception.StandardException;
import com.sugan.gradlespringboot.mapper.EmpMapper;
import com.sugan.gradlespringboot.service.EmpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Demo Rest API", description = "Demo API for testing DevSecOps")
@Validated
public class HelloController {
    private final EmpService empService;

    public HelloController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/")
    @Operation(summary = "Home API", description = "Home API to test the Application")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    public ResponseEntity<String> getWelcome() {
        return ResponseEntity.status(200).body("Welcome to Gradle Spring boot");
    }

    @PostMapping("/createEmp")
    @Operation(summary = "CreateEmp", description = "New Employee Creation")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})

    public ResponseEntity<String> saveEmp(@Valid @RequestBody EmpRequest empRequest) {
        try {
            String response = empService.saveEmployee(empRequest);
            return ResponseEntity.status(200).body("Your Employee Id is :  " + response);
        }catch(Exception exception){
            throw new StandardException(exception.getMessage(), HttpStatus.BAD_REQUEST.toString());
        }
    }
}
