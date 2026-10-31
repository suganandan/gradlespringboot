// SONAR-FIX [I-001 java:S2696] Removed the modification of a static field from an instance method to prevent potential concurrency issues and simplify the code.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that removing the static callCount variable and its increments does not impact any intended debugging or tracking logic.
// SONAR-FIX [I-002 java:S2696] Removed the modification of a static field from an instance method to prevent potential concurrency issues and simplify the code.
// HUMAN_REVIEW_REQUIRED [I-002]: Verify that removing the static callCount variable and its increments does not impact any intended debugging or tracking logic.
// SONAR-FIX [I-003 java:S106] Removed System.out.println as it was tied to the removed static callCount variable and redundant with existing logger.
// HUMAN_REVIEW_REQUIRED [I-003]: Confirm that the existing log.info statement is sufficient for logging pre-update events.
package com.sugan.gradlespringboot.listener;

import com.sugan.gradlespringboot.entity.Emptbl;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditListener {

    // SONAR-FIX [I-001 java:S2696] Removed the static field as it was modified by instance methods, leading to potential concurrency issues.
    // HUMAN_REVIEW_REQUIRED [I-001]: Confirm that this static counter is not needed for any critical application logic.

    @PreUpdate
    private void preUpdate(Emptbl entity2) {
        // SONAR-FIX [I-001 java:S2696] Removed the increment of a static field from an instance method.
        // HUMAN_REVIEW_REQUIRED [I-001]: Ensure no critical logic relied on this counter.
        // SONAR-FIX [I-003 java:S106] Removed System.out.println as it was tied to the removed static callCount variable and redundant with existing logger.
        // HUMAN_REVIEW_REQUIRED [I-003]: Confirm that the existing log.info statement is sufficient for logging pre-update events.
        log.info("PreUpdate called for: {}", entity2.toString());
    }

    @PrePersist
    private void prePersist(Emptbl entity2) {
        // SONAR-FIX [I-002 java:S2696] Removed the increment of a static field from an instance method.
        // HUMAN_REVIEW_REQUIRED [I-002]: Ensure no critical logic relied on this counter.
        log.info("PrePersist called for: {}", entity2.toString());
    }

    @PostLoad
    private void postLoad(Emptbl entity3) {

        log.info("postLoad called for: {}", entity3.toString());
    }

    @PostUpdate
    private void postUpdate(Emptbl entity3) {

        log.info("PostUpdate called for: {}", entity3.toString());
    }

    @PostPersist
    private void postPersist(Emptbl entity3) {

        log.info("PostPersist called for: {}", entity3.toString());
    }

    public void audit(Object target) {
        // TODO store the audit trail in a table
        // SONAR-FIX [I-004 java:S108] Added a comment to explain why this block is empty, as the method is currently a placeholder.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify that this empty block is intentionally a placeholder for future implementation.
        if (target == null) { /* This block is intentionally empty as the audit logic is not yet implemented. */
        } else {
            // SONAR-FIX [I-005 java:S106] Replaced System.out.println with a logger for proper logging practices.
            // HUMAN_REVIEW_REQUIRED [I-005]: Confirm that logging the target object via log.info is the desired behavior.
            log.info("Audit target: {}", target);
        }
    }
}
