package com.sugan.gradlespringboot;

// SONAR-FIX [I-005 java:S1128] Removed unused import 'java.util.ArrayList'.
// HUMAN_REVIEW_REQUIRED [I-005]: Verify that ArrayList is not used anywhere in the file.
// SONAR-FIX [I-006 java:S1128] Removed unused import 'java.util.Date'.
// HUMAN_REVIEW_REQUIRED [I-006]: Verify that Date is not used anywhere in the file.
// SONAR-FIX [I-007 java:S1128] Removed unused import 'java.util.List'.
// HUMAN_REVIEW_REQUIRED [I-007]: Verify that List is not used anywhere in the file.

// SONAR-FIX [I-003 java:S106] Added import for Logger to replace System.out.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the correct logging framework (SLF4J) is used.
import org.slf4j.Logger;
// SONAR-FIX [I-003 java:S106] Added import for LoggerFactory to replace System.out.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the correct logging framework (SLF4J) is used.
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GradlespringbootApplication {

    // SONAR-FIX [I-003 java:S106] Declared a static logger for the class.
    // HUMAN_REVIEW_REQUIRED [I-003]: Verify that the logger is correctly initialized.
    private static final Logger log = LoggerFactory.getLogger(GradlespringbootApplication.class);

    // SONAR-FIX [I-001 java:S1068] Removed unused private field ADMIN_PASSWORD.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify that ADMIN_PASSWORD is not used anywhere in the application.

    public static void main(String[] args) {
        // SONAR-FIX [I-002 java:S1854] Removed useless assignment to local variable "unusedBanner".
        // SONAR-FIX [I-008 java:S1481] Removed unused local variable "unusedBanner".
        // HUMAN_REVIEW_REQUIRED [I-002]: Verify that removing this assignment does not change behavior.
        // HUMAN_REVIEW_REQUIRED [I-008]: Verify that removing this variable does not change behavior.
        // The line 'String unusedBanner = "starting";' was here.

        // SONAR-FIX [I-003 java:S106] Replaced System.out.println with logger.info.
        // HUMAN_REVIEW_REQUIRED [I-003]: Verify that logging output is correctly routed to the application's logging system.
        log.info("Starting application");
        SpringApplication.run(GradlespringbootApplication.class, args);
    }

    // SONAR-FIX [I-004 java:S125] Removed commented-out block of code.
    // HUMAN_REVIEW_REQUIRED [I-004]: Verify that the removed code is indeed dead and not needed.
    // The lines '// TODO replace the hard-coded admin password with a secret', '// List<String> debug = new ArrayList<>();',
    // '// debug.add("started");', '// System.out.println(debug);' were here.

}
