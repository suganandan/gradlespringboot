package com.sugan.gradlespringboot;

// SONAR-FIX [I-005 java:S1128] Removed unused import 'java.util.ArrayList'.
// HUMAN_REVIEW_REQUIRED [I-005]: Verify that 'java.util.ArrayList' is not used elsewhere in the file.
// SONAR-FIX [I-006 java:S1128] Removed unused import 'java.util.Date'.
// HUMAN_REVIEW_REQUIRED [I-006]: Verify that 'java.util.Date' is not used elsewhere in the file.
// SONAR-FIX [I-007 java:S1128] Removed unused import 'java.util.List'.
// HUMAN_REVIEW_REQUIRED [I-007]: Verify that 'java.util.List' is not used elsewhere in the file.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
// SONAR-FIX [I-003 java:S106] Added import for Logger.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the correct logging library is used.
import org.slf4j.Logger;
// SONAR-FIX [I-003 java:S106] Added import for LoggerFactory.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the correct logging library is used.
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class GradlespringbootApplication {

	// SONAR-FIX [I-001 java:S1068] Removed unused private field "ADMIN_PASSWORD".
	// HUMAN_REVIEW_REQUIRED [I-001]: Verify that "ADMIN_PASSWORD" is not used anywhere in the application.

	// SONAR-FIX [I-003 java:S106] Added a logger for proper logging instead of System.out.
	// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the logger is correctly initialized and used.
	private static final Logger logger = LoggerFactory.getLogger(GradlespringbootApplication.class);

	public static void main(String[] args) {
		// SONAR-FIX [I-002 java:S1854] Removed useless assignment to local variable "unusedBanner".
		// HUMAN_REVIEW_REQUIRED [I-002]: Verify that removing this assignment does not affect application logic.
		// SONAR-FIX [I-008 java:S1481] Removed unused local variable "unusedBanner".
		// HUMAN_REVIEW_REQUIRED [I-008]: Verify that removing this variable does not affect application logic.
		// SONAR-FIX [I-003 java:S106] Replaced System.out.println with logger.info for proper logging.
		// HUMAN_REVIEW_REQUIRED [I-003]: Verify that the logging message is appropriate.
		logger.info("Starting application");
		SpringApplication.run(GradlespringbootApplication.class, args);
	}

	// SONAR-FIX [I-004 java:S125] Removed commented-out block of code.
	// HUMAN_REVIEW_REQUIRED [I-004]: Verify that the removed commented-out code is not needed.

}
