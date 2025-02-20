package com.sugan.gradlespringboot.controller;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.dto.ErrorResponseDto;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.service.EmpService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Employee API", description = "CRUD API for Employees")
@Validated  // Enables Validation at Controller Level
@RequestMapping("/api/employee")
public class HelloController {

    private final EmpService empService;

    public HelloController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getWelcome() {
        return ResponseEntity.status(200).body("Welcome to Gradle Spring Boot");
    }

    @Operation(summary = "Create Employee", description = "Creates a new employee record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Employee Created Successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Input",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - Unexpected issue",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<String> saveEmp(@Valid @RequestBody EmpRequest empRequest) {
        String response = empService.saveEmployee(empRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee ID created: " + response);
    }


    @GetMapping("/fetch/{id}")
    @Operation(summary = "Fetch Employee", description = "Retrieves an employee by ID")
    @Retry(name = "fetchEmployeeRetry", fallbackMethod = "fetchEmployeeFallback")
    public ResponseEntity<Emptbl> fetchEmp(
            @PathVariable @Min(value = 1, message = "Employee ID must be greater than 0") Long id) {
        return ResponseEntity.ok(empService.getEmployeeById(id));
    }

    public ResponseEntity<Emptbl> fetchEmployeeFallback(Long id) {
        Emptbl fallbackEmp = new Emptbl();
        fallbackEmp.setEmpid(id);
        fallbackEmp.setName("Unknown Employee");
        return ResponseEntity.ok(fallbackEmp);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Emptbl>> fetchAllEmps() {
        return ResponseEntity.ok(empService.getAllEmployees());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Employee", description = "Updates an existing employee")
    public ResponseEntity<String> updateEmp(
            @PathVariable @Min(value = 1, message = "Employee ID must be greater than 0") Long id,
            @Valid @RequestBody EmpRequest empRequest) {
        boolean isUpdated = empService.updateEmployee(id, empRequest);
        return isUpdated ? ResponseEntity.ok("Employee Updated")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Employee", description = "Deletes an employee")
    public ResponseEntity<String> deleteEmp(
            @PathVariable @Min(value = 1, message = "Employee ID must be greater than 0") Long id) {
        boolean isDeleted = empService.deleteEmployee(id);
        return isDeleted ? ResponseEntity.ok("Employee Deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
    }
}
