package com.sugan.gradlespringboot.listener;

import com.sugan.gradlespringboot.entity.Emptbl;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditListener {

    @PreUpdate
    private void preUpdate(Emptbl entity2) {
        // I-001: Removed problematic static field modification.
        // I-003: Replaced System.out with logger.
        log.info("PreUpdate called.");
        log.info("PreUpdate called for: {}", entity2.toString());
    }

    @PrePersist
    private void prePersist(Emptbl entity2) {
        // I-002: Removed problematic static field modification.
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
        // I-004: Removed empty if block.
        if (target != null) {
            // I-005: Replaced System.out with logger.
            log.info("Auditing target: {}", target);
        }
    }
}
