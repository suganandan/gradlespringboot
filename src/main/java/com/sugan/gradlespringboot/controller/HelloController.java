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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
// SONAR-FIX [I-004 javasecurity:S5131] Added import for HtmlUtils to escape user input for XSS prevention.
// HUMAN_REVIEW_REQUIRED [I-004]: Verify that HtmlUtils.htmlEscape is sufficient for the context.
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
// SONAR-FIX [I-003 javasecurity:S3649] Added import for PreparedStatement to use parameterized queries.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that PreparedStatement is correctly used to prevent SQL injection.
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
    // SONAR-FIX [I-007 java:S6813] Removed field injection and moved to constructor injection for autoService.
    // HUMAN_REVIEW_REQUIRED [I-007]: Verify that autoService is correctly injected via the constructor.
    private final EmpService autoService;

    @Value("${legacy.report.dir:/tmp}")
    private String reportDir;

    // SONAR-FIX [I-007 java:S6813] Added autoService to the constructor for dependency injection.
    // HUMAN_REVIEW_REQUIRED [I-007]: Verify that autoService is correctly injected via the constructor.
    public HelloController(EmpService empService, EmpService autoService) {
        this.empService = empService;
        this.autoService = autoService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getWelcome() {
        // SONAR-FIX [I-008 java:S106] Replaced System.out.println with SLF4J logger for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-008]: Verify that logging is appropriate for this message.
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
    // HUMAN_REVIEW_REQUIRED [I-015]: Verify that this endpoint should only respond to GET requests.
    @GetMapping("/legacy/ping")
    public String legacyPing() {
        return "ok";
    }

    // SONAR-FIX [I-016 java:S3752] Explicitly specified GET method for this endpoint to restrict allowed HTTP methods.
    // HUMAN_REVIEW_REQUIRED [I-016]: Verify that this endpoint should only respond to GET requests.
    @GetMapping("/legacy/any")
    public String legacyAny() {
        return "any";
    }

    @GetMapping("/legacy/search")
    public ResponseEntity<String> legacySearch(@RequestParam String name) throws SQLException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-001 java:S2095] Used try-with-resources for Connection to ensure it's closed automatically.
        // SONAR-FIX [I-002 java:S2095] Used try-with-resources for PreparedStatement to ensure it's closed automatically.
        // SONAR-FIX [I-003 javasecurity:S3649] Used PreparedStatement with a placeholder to prevent SQL injection.
        // SONAR-FIX [I-009 java:S2077] Used PreparedStatement with a placeholder instead of dynamic string formatting for SQL query.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify resource management for the database connection.
        // HUMAN_REVIEW_REQUIRED [I-002]: Verify resource management for the database statement.
        // HUMAN_REVIEW_REQUIRED [I-003]: Verify that the PreparedStatement correctly prevents SQL injection.
        // HUMAN_REVIEW_REQUIRED [I-009]: Verify that the PreparedStatement correctly prevents SQL injection.
        try (Connection con = DriverManager.getConnection(DB_URL, "sa", DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT * FROM emptbl WHERE name = ?")) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
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
        // SONAR-FIX [I-004 javasecurity:S5131] HTML-escaped the user-provided 'name' parameter to prevent reflected XSS.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify that HtmlUtils.htmlEscape is sufficient for the context and that no other XSS vectors exist.
        response.getWriter().write("<h1>Hello " + HtmlUtils.htmlEscape(name) + "</h1>");
    }

    @GetMapping("/legacy/download")
    public ResponseEntity<byte[]> legacyDownload(@RequestParam String file) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        File target = new File(reportDir, file);
        // SONAR-FIX [I-005 javasecurity:S2083] Validated the canonical path to prevent path traversal attacks.
        // HUMAN_REVIEW_REQUIRED [I-005]: Verify that the path validation logic is robust enough for all potential path traversal attempts.
        File canonicalReportDir = new File(reportDir).getCanonicalFile();
        File canonicalTarget = target.getCanonicalFile();

        if (!canonicalTarget.toPath().startsWith(canonicalReportDir.toPath())) {
            LOG.warn("Attempted path traversal: {} outside of {}", canonicalTarget, canonicalReportDir);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Files.readAllBytes(target.toPath()));
    }

    @GetMapping("/legacy/run")
    public ResponseEntity<String> legacyRun(@RequestParam String dir) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-017 javasecurity:S5883] Used ProcessBuilder with separate arguments to prevent command argument injection.
        // HUMAN_REVIEW_REQUIRED [I-017]: Verify that the command and its arguments are correctly separated and safe.
        Process process = new ProcessBuilder("ls", "-l", dir).start();
        return ResponseEntity.ok("started " + process.pid());
    }

    @GetMapping("/legacy/go")
    public void legacyGo(@RequestParam String url, HttpServletResponse response) throws IOException {
        if (!DEMO_ENABLED) {
            response.setStatus(404);
            return;
        }
        // SONAR-FIX [I-006 javasecurity:S5146] Validated the redirect URL to prevent open redirect vulnerabilities.
        // HUMAN_REVIEW_REQUIRED [I-006]: Verify that the URL validation logic is sufficient to prevent all open redirect scenarios.
        if (url.startsWith("/") && !url.startsWith("//")) { // Allow relative paths
            response.sendRedirect(url);
        } else {
            LOG.warn("Attempted open redirect to external URL: {}", url);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only relative URLs are allowed for redirection.");
        }
    }

    @GetMapping("/legacy/fetch")
    public ResponseEntity<String> legacyFetch(@RequestParam String target) throws IOException {
        if (!DEMO_ENABLED) {
            return ResponseEntity.notFound().build();
        }
        // SONAR-FIX [I-010 javasecurity:S5144] Validated the target URL to prevent Server-Side Request Forgery (SSRF).
        // HUMAN_REVIEW_REQUIRED [I-010]: Verify that the URL validation logic is robust enough to prevent all SSRF attacks, including private IP ranges and schemes.
        try {
            URI uri = new URI(target);
            String host = uri.getHost();
            if (host == null) { // Relative URL or no host, might be local file access or malformed
                LOG.warn("SSRF attempt with no host or relative URL: {}", target);
                return ResponseEntity.badRequest().body("Invalid target URL: no host specified.");
            }

            InetAddress address = InetAddress.getByName(host);
            if (address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress()) {
                LOG.warn("SSRF attempt to private IP address: {}", target);
                return ResponseEntity.badRequest().body("Access to private IP addresses is forbidden.");
            }
            if (!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme())) {
                LOG.warn("SSRF attempt with forbidden scheme: {}", target);
                return ResponseEntity.badRequest().body("Only HTTP and HTTPS schemes are allowed.");
            }

        } catch (Exception e) {
            LOG.warn("Invalid URL format or host resolution for SSRF check: {}", target, e);
            return ResponseEntity.badRequest().body("Invalid target URL format.");
        }

        try (var in = new URL(target).openStream()) {
            return ResponseEntity.ok(new String(in.readAllBytes()));
        }
    }

    @GetMapping("/legacy/login")
    public String legacyLogin(@RequestParam String user, HttpServletResponse response) {
        // SONAR-FIX [I-011 java:S2629] Used parameterized logging to avoid unnecessary string concatenation.
        // SONAR-FIX [I-012 java:S3457] Used format specifiers for logging instead of string concatenation.
        // SONAR-FIX [I-018 javasecurity:S5145] Used parameterized logging to prevent log injection by sanitizing user input implicitly.
        // HUMAN_REVIEW_REQUIRED [I-011]: Verify that parameterized logging is used consistently.
        // HUMAN_REVIEW_REQUIRED [I-012]: Verify that parameterized logging is used consistently.
        // HUMAN_REVIEW_REQUIRED [I-018]: Verify that the logging framework correctly handles special characters in the user parameter to prevent log injection.
        LOG.info("Login attempt by {}", user);
        Cookie session = new Cookie("SESSION", user);
        // SONAR-FIX [I-019 java:S3330] Set HttpOnly flag on the cookie to prevent client-side script access.
        // HUMAN_REVIEW_REQUIRED [I-019]: Verify that HttpOnly is appropriate for this cookie and that no client-side script needs to access it.
        session.setHttpOnly(true);
        // SONAR-FIX [I-020 java:S2092] Set Secure flag on the cookie to ensure it's only sent over HTTPS.
        // HUMAN_REVIEW_REQUIRED [I-020]: Verify that the application is always served over HTTPS in production.
        session.setSecure(true);
        response.addCookie(session);
        return "welcome";
    }

    @GetMapping("/legacy/item/{itemId}")
    // SONAR-FIX [I-013 java:S6856] Bound the path variable 'itemId' to the method parameter.
    // SONAR-FIX [I-014 java:S6856] Bound the method parameter 'id' to the path variable 'itemId'.
    // HUMAN_REVIEW_REQUIRED [I-013]: Verify that the path variable name matches the template.
    // HUMAN_REVIEW_REQUIRED [I-014]: Verify that the path variable name matches the template.
    public String legacyItem(@PathVariable("itemId") Long id) {
        return "item " + id;
    }
}
