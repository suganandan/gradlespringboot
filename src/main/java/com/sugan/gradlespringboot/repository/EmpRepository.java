package com.sugan.gradlespringboot.repository;

import com.sugan.gradlespringboot.entity.Emptbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// SONAR-FIX [I-002 java:S1128] Removed unused import statement.
// HUMAN_REVIEW_REQUIRED [I-002]: Verify that 'java.util.Optional' is not used anywhere else in this file.

// SONAR-FIX [I-001 java:S106] Added SLF4J Logger import for proper logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that SLF4J is the intended logging framework.
import org.slf4j.Logger;
// SONAR-FIX [I-001 java:S106] Added SLF4J LoggerFactory import for proper logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that SLF4J is the intended logging framework.
import org.slf4j.LoggerFactory;

@Repository
public interface EmpRepository extends JpaRepository<Emptbl, Long>{

    // SONAR-FIX [I-001 java:S106] Added a static logger instance to replace System.out.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify the logger instance is correctly initialized and used.
    static final Logger logger = LoggerFactory.getLogger(EmpRepository.class);

    default List findRaw() {
        return findAll();
    }

    default boolean isEmptyTable() {
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with a logger statement for better logging practices.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify the log message is appropriate and the logging level is correct.
        logger.info("checking table");
        // SONAR-FIX [I-003 java:S1155] Used isEmpty() method for checking collection emptiness for clarity and efficiency.
        // HUMAN_REVIEW_REQUIRED [I-003]: Verify that isEmpty() correctly reflects the intended logic.
        return findAll().isEmpty();
    }
}
