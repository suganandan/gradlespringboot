// SONAR-FIX [I-001 java:S2696] Added import for AtomicInteger to enable thread-safe static field updates.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the new import is correct.
package com.sugan.gradlespringboot.listener;

import com.sugan.gradlespringboot.entity.Emptbl;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AuditListener {

    // SONAR-FIX [I-001 java:S2696] Changed static int to AtomicInteger for thread-safe updates from non-static methods.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify that using AtomicInteger is appropriate for this counter's purpose.
    // SONAR-FIX [I-002 java:S2696] Changed static int to AtomicInteger for thread-safe updates from non-static methods.
    // HUMAN_REVIEW_REQUIRED [I-002]: Verify that using AtomicInteger is appropriate for this counter's purpose.
    private static final AtomicInteger callCount = new AtomicInteger(0);

    @PreUpdate
    private void preUpdate(Emptbl entity2) {
        // SONAR-FIX [I-001 java:S2696] Replaced direct increment of static int with AtomicInteger's incrementAndGet for thread safety.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify the counter update is correct and thread-safe.
        int currentCallCount = callCount.incrementAndGet();
        // SONAR-FIX [I-003 java:S106] Replaced System.out.println with logger.
        // HUMAN_REVIEW_REQUIRED [I-003]: Verify the log message content and level are appropriate.
        log.info("PreUpdate call count: {}", currentCallCount);
        log.info("PreUpdate called for: {}", entity2.toString());
    }

    @PrePersist
    private void prePersist(Emptbl entity2) {
        // SONAR-FIX [I-002 java:S2696] Replaced direct increment of static int with AtomicInteger's incrementAndGet for thread safety.
        // HUMAN_REVIEW_REQUIRED [I-002]: Verify the counter update is correct and thread-safe.
        callCount.incrementAndGet();
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
        // SONAR-FIX [I-004 java:S108] Added a comment to explain the intentionally empty block.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify the comment accurately reflects the intent of the empty block.
        if (target == null) {
            // No action needed when target is null.
        } else {
            // SONAR-FIX [I-005 java:S106] Replaced System.out.println with logger.
            // HUMAN_REVIEW_REQUIRED [I-005]: Verify the log message content and level are appropriate.
            log.info("Auditing target: {}", target);
        }
    }
}
