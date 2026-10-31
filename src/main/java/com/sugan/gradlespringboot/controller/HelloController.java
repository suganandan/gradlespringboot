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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
@Tag(name = "Employee API", description = "CRUD API for Employees")
@Validated  // Enables Validation at Controller Level
@RequestMapping("/api/employee")
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);
    private static final boolean DEMO_ENABLED = Boolean.getBoolean("legacy.demo");
    private static final String DB_URL = "jdbc:h2:mem:legacy";
    private static final String DB_PASSWORD = "sa-password-1";

    private final EmpService empService;

    @Value("${legacy.report.dir:/tmp}")
    private String reportDir;

    @Value("${legacy.ssrf.allowed-hosts:localhost,127.0.0.1}")
    private String allowedSsrfHosts;

    public HelloController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getWelcome() {
        LOG.info("Welcome endpoint called");
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

    @GetMapping("/legacy/ping")
    public String legacyPing() {
        return "ok";
    }

    @GetMapping("/legacy/any")
    public String legacyAny() {
        return "any";
    }

    @GetMapping("/legacy/search")
    public ResponseEntity<String> legacySearch(@RequestParam String name) throws SQLException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        String found = null;
        try (Connection con = DriverManager.getConnection(DB_URL, "sa", DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement("SELECT empid FROM emptbl WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    found = rs.getString(1);
                }
            }
        }
        return ResponseEntity.ok(found);
    }

    @GetMapping("/legacy/hello")
    public void legacyHello(@RequestParam String name, HttpServletResponse response) throws IOException {
        if (!DEMO_ENABLED) {
            response.setStatus(404);
            return;
        }
        response.getWriter().write("<h1>Hello " + HtmlUtils.htmlEscape(name) + "</h1>");
    }

    @GetMapping("/legacy/download")
    public ResponseEntity<byte[]> legacyDownload(@RequestParam String file) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        Path baseDir = Paths.get(reportDir);
        Path resolvedPath = baseDir.resolve(file).normalize();

        if (!resolvedPath.startsWith(baseDir)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Files.readAllBytes(resolvedPath));
    }

    @GetMapping("/legacy/run")
    public ResponseEntity<String> legacyRun(@RequestParam String dir) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        if (dir.contains("/") || dir.contains("..") || dir.contains(" ") || dir.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid directory name.");
        }
        ProcessBuilder pb = new ProcessBuilder("ls", "-l", dir);
        Process process = pb.start();
        return ResponseEntity.ok("started " + process.pid());
    }

    @GetMapping("/legacy/go")
    public void legacyGo(@RequestParam String url, HttpServletResponse response) throws IOException {
        if (!DEMO_ENABLED) {
            response.setStatus(404);
            return;
        }
        if (!url.startsWith("/") || url.startsWith("//")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.sendRedirect(url);
    }

    @GetMapping("/legacy/fetch")
    public ResponseEntity<String> legacyFetch(@RequestParam String target) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        URL urlObj = new URL(target);
        String protocol = urlObj.getProtocol();
        if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
            return ResponseEntity.badRequest().body("Only HTTP/HTTPS schemes are allowed.");
        }
        String host = urlObj.getHost();
        if (!Arrays.asList(allowedSsrfHosts.split(",")).contains(host)) {
            return ResponseEntity.badRequest().body("Host not allowed.");
        }

        try (var in = urlObj.openStream()) {
            return ResponseEntity.ok(new String(in.readAllBytes()));
        }
    }

    @GetMapping("/legacy/login")
    public String legacyLogin(@RequestParam String user, HttpServletResponse response) {
        String sanitizedUser = user.replaceAll("[\r\n]", "_");
        LOG.info("Login attempt by {}", sanitizedUser);
        Cookie session = new Cookie("SESSION", user);
        session.setHttpOnly(true);
        session.setSecure(true);
        session.setPath("/");
        response.addCookie(session);
        return "welcome";
    }

    @GetMapping("/legacy/item/{itemId}")
    public String legacyItem(@PathVariable("itemId") Long id) {
        return "item " + id;
    }
}
