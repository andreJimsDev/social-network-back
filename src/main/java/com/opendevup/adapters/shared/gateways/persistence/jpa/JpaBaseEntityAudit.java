package com.opendevup.adapters.shared.gateways.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaBaseEntityAudit extends JpaBaseEntity {
    private String createdBy;
    private String lastModifiedBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaBaseEntityAudit that)) return false;
        if (!super.equals(o)) return false;
        return createdBy.equals(that.createdBy) &&
                lastModifiedBy.equals(that.lastModifiedBy) &&
                createdDate.equals(that.createdDate) &&
                lastModifiedDate.equals(that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                createdBy, lastModifiedBy, createdDate, lastModifiedDate);
    }

    @Override
    public String toString() {
        return "JpaBaseEntityAudit{" +
                "createdBy='" + createdBy +
                ", lastModifiedBy='" + lastModifiedBy +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                "}" +
                super.toString();
    }
}