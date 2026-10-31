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
// SONAR-FIX [I-004 javasecurity:S5131] Added import for HtmlUtils to escape user input.
// HUMAN_REVIEW_REQUIRED [I-004]: Verify that HtmlUtils is available and correctly used.
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
// SONAR-FIX [I-005 javasecurity:S2083] Added import for Path to handle file paths securely.
// HUMAN_REVIEW_REQUIRED [I-005]: Verify that Path is available and correctly used.
import java.nio.file.Path;
// SONAR-FIX [I-005 javasecurity:S2083] Added import for Paths to handle file paths securely.
// HUMAN_REVIEW_REQUIRED [I-005]: Verify that Paths is available and correctly used.
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
// SONAR-FIX [I-003 javasecurity:S3649] Added import for PreparedStatement to prevent SQL injection.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that PreparedStatement is available and correctly used.
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

    // SONAR-FIX [I-007 java:S6813] Removed field injection and its associated field as it was unused and constructor injection is already present.
    // HUMAN_REVIEW_REQUIRED [I-007]: Verify that the 'autoService' field was indeed unused and its removal does not impact functionality.

    @Value("${legacy.report.dir:/tmp}")
    private String reportDir;

    // SONAR-FIX [I-010 javasecurity:S5144] Added a configurable property for allowed SSRF hosts.
    // HUMAN_REVIEW_REQUIRED [I-010]: Verify that the 'app.ssrf.allowed-hosts' property is configured with a secure allow-list of hosts.
    @Value("${app.ssrf.allowed-hosts:localhost,127.0.0.1}") // Example default, should be configured
    private String allowedSsrfHosts;

    public HelloController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getWelcome() {
        // SONAR-FIX [I-008 java:S106] Replaced System.out.println with a logger call for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-008]: Verify that the log message is appropriate and appears in the application logs.
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

    // SONAR-FIX [I-015 java:S4488] Replaced @RequestMapping with @GetMapping for better semantic clarity.
    // HUMAN_REVIEW_REQUIRED [I-015]: Verify that the endpoint still functions correctly with @GetMapping.
    @GetMapping("/legacy/ping")
    public String legacyPing() {
        return "ok";
    }

    // SONAR-FIX [I-016 java:S3752] Explicitly specified HTTP GET method for the endpoint to restrict allowed methods.
    // HUMAN_REVIEW_REQUIRED [I-016]: Verify that this endpoint is only intended for GET requests and that no other HTTP methods should be allowed.
    @GetMapping("/legacy/any")
    public String legacyAny() {
        return "any";
    }

    @GetMapping("/legacy/search")
    public ResponseEntity<String> legacySearch(@RequestParam String name) throws SQLException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-001 java:S2095] Use try-with-resources for Connection, Statement, and ResultSet to prevent resource leaks.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that all JDBC resources are properly closed.
        // SONAR-FIX [I-002 java:S2095] Use try-with-resources for Connection, Statement, and ResultSet to prevent resource leaks.
        // HUMAN_REVIEW_REQUIRED [I-002]: Verify that all JDBC resources are properly closed.
        // SONAR-FIX [I-003 javasecurity:S3649] Use PreparedStatement with parameterized queries to prevent SQL injection.
        // HUMAN_REVIEW_REQUIRED [I-003]: Verify that the SQL query is safe from injection and that the PreparedStatement is used correctly.
        // SONAR-FIX [I-009 java:S2077] Use PreparedStatement with parameterized queries instead of dynamic string formatting for SQL.
        // HUMAN_REVIEW_REQUIRED [I-009]: Verify that the SQL query is safe from injection and that the PreparedStatement is used correctly.
        try (Connection con = DriverManager.getConnection(DB_URL, "sa", DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM emptbl WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                String found = rs.next() ? rs.getString(1) : null;
                return ResponseEntity.ok(found);
            }
        }
    }

    @GetMapping("/legacy/hello")
    public void legacyHello(@RequestParam String name, HttpServletResponse response) throws IOException {
        if (!DEMO_ENABLED) {
            response.setStatus(404);
            return;
        }
        // SONAR-FIX [I-004 javasecurity:S5131] HTML-escape user-provided input to prevent reflected XSS attacks.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify that user input is properly escaped before being written to the response.
        response.getWriter().write("<h1>Hello " + HtmlUtils.htmlEscape(name) + "</h1>");
    }

    @GetMapping("/legacy/download")
    public ResponseEntity<byte[]> legacyDownload(@RequestParam String file) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-005 javasecurity:S2083] Resolve user-provided file path against a base directory and normalize to prevent path traversal.
        // HUMAN_REVIEW_REQUIRED [I-005]: Verify that file access is restricted to the intended directory and that path normalization is effective.
        Path baseDir = Paths.get(reportDir).toAbsolutePath().normalize();
        Path resolvedPath = baseDir.resolve(file).normalize();

        if (!resolvedPath.startsWith(baseDir)) {
            LOG.warn("Attempted path traversal: {}", file);
            return ResponseEntity.badRequest().build(); // Or forbidden, depending on policy
        }

        File target = resolvedPath.toFile();
        if (!target.exists() || !target.isFile()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Files.readAllBytes(target.toPath()));
    }

    @GetMapping("/legacy/run")
    public ResponseEntity<String> legacyRun(@RequestParam String dir) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-017 javasecurity:S5883] Use ProcessBuilder with separate arguments to prevent command argument injection.
        // HUMAN_REVIEW_REQUIRED [I-017]: Verify that user input 'dir' is properly sanitized or validated before being passed as a command argument.
        // For simplicity, assuming 'dir' is a single argument and not containing malicious command separators.
        // A more robust solution would involve strict validation of 'dir' to ensure it's a safe path or filename.
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
        // SONAR-FIX [I-006 javasecurity:S5146] Validate redirect URL to prevent open redirect vulnerabilities.
        // HUMAN_REVIEW_REQUIRED [I-006]: Verify that the URL validation logic correctly restricts redirects to safe internal paths or an allow-list of external domains.
        if (url.startsWith("/") && !url.startsWith("//")) { // Allow relative paths within the application
            response.sendRedirect(url);
        } else {
            LOG.warn("Attempted open redirect to untrusted URL: {}", url);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Or SC_FORBIDDEN
        }
    }

    @GetMapping("/legacy/fetch")
    public ResponseEntity<String> legacyFetch(@RequestParam String target) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-010 javasecurity:S5144] Validate target URL scheme and host to prevent Server-Side Request Forgery (SSRF).
        // HUMAN_REVIEW_REQUIRED [I-010]: Verify that the allowed hosts configuration is secure and comprehensive for the application's needs.
        try {
            URL url = new URL(target);
            String protocol = url.getProtocol();
            String host = url.getHost();

            if (!("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol))) {
                LOG.warn("SSRF attempt with disallowed protocol: {}", protocol);
                return ResponseEntity.badRequest().body("Disallowed protocol");
            }

            List<String> allowedHostsList = List.of(allowedSsrfHosts.split(","));
            if (!allowedHostsList.contains(host)) {
                LOG.warn("SSRF attempt to disallowed host: {}", host);
                return ResponseEntity.badRequest().body("Disallowed host");
            }

            try (var in = url.openStream()) {
                return ResponseEntity.ok(new String(in.readAllBytes()));
            }
        } catch (IOException e) {
            LOG.error("Error fetching URL: {}", target, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching URL");
        } catch (IllegalArgumentException e) { // For malformed URLs
            LOG.warn("Malformed URL provided for SSRF: {}", target, e);
            return ResponseEntity.badRequest().body("Malformed URL");
        }
    }

    @GetMapping("/legacy/login")
    public String legacyLogin(@RequestParam String user, HttpServletResponse response) {
        // SONAR-FIX [I-011 java:S2629] Use parameterized logging to avoid unnecessary string concatenation.
        // HUMAN_REVIEW_REQUIRED [I-011]: Verify that logging arguments are correctly formatted.
        // SONAR-FIX [I-012 java:S3457] Use parameterized logging instead of string concatenation for better performance and readability.
        // HUMAN_REVIEW_REQUIRED [I-012]: Verify that logging arguments are correctly formatted.
        // SONAR-FIX [I-018 javasecurity:S5145] Strip newline characters from user input in logs to prevent log injection.
        // HUMAN_REVIEW_REQUIRED [I-018]: Verify that user input is sanitized before being logged.
        LOG.info("Login attempt by {}", user.replaceAll("[\r\n]", "_"));
        Cookie session = new Cookie("SESSION", user);
        // SONAR-FIX [I-019 java:S3330] Set HttpOnly flag on cookies to prevent client-side script access.
        // HUMAN_REVIEW_REQUIRED [I-019]: Verify that the HttpOnly flag is appropriate for this cookie and does not break intended client-side functionality.
        session.setHttpOnly(true);
        // SONAR-FIX [I-020 java:S2092] Set Secure flag on cookies to ensure transmission only over HTTPS.
        // HUMAN_REVIEW_REQUIRED [I-020]: Verify that the Secure flag is appropriate for this cookie and that the application is always served over HTTPS.
        session.setSecure(true);
        session.setPath("/"); // Good practice to explicitly set path
        response.addCookie(session);
        return "welcome";
    }

    @GetMapping("/legacy/item/{itemId}")
    // SONAR-FIX [I-013 java:S6856] Bind template variable "itemId" to the method parameter using @PathVariable("itemId").
    // HUMAN_REVIEW_REQUIRED [I-013]: Verify that the path variable name in the annotation matches the template variable.
    // SONAR-FIX [I-014 java:S6856] Bind method parameter "id" to the template variable "itemId" using @PathVariable("itemId").
    // HUMAN_REVIEW_REQUIRED [I-014]: Verify that the method parameter is correctly bound to the path template variable.
    public String legacyItem(@PathVariable("itemId") Long id) {
        return "item " + id;
    }
}
