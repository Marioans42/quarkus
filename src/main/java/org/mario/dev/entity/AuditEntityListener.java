package org.mario.dev.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditEntityListener {
    @PrePersist
    void preCreate(AbstractEntity auditable) {
        Instant now = Instant.now();
        auditable.setCreatedDate(now);
        auditable.setLastModifiedDate(now);
    }
    @PreUpdate
    void preUpdate(AbstractEntity auditable) {
        Instant now = Instant.now();
        auditable.setLastModifiedDate(now);
    }
}
