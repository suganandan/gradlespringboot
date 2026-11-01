package com.sugan.gradlespringboot.listener;

import com.sugan.gradlespringboot.entity.Emptbl;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AuditListener {

    private static final AtomicInteger callCount = new AtomicInteger(0);

    @PreUpdate
    private void preUpdate(Emptbl entity2) {
        int currentCallCount = callCount.incrementAndGet();
        log.info("PreUpdate call count: {}", currentCallCount);
        log.info("PreUpdate called for: {}", entity2.toString());
    }

    @PrePersist
    private void prePersist(Emptbl entity2) {
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
        if (target != null) {
            log.info("Audit target: {}", target);
        }
    }
}
