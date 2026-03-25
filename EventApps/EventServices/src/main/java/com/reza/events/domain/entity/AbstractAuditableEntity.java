package com.reza.events.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractAuditableEntity extends AbstractEntity {

    @Audited(targetAuditMode = NOT_AUDITED)
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", updatable = false)
    @DiffIgnore
    private AuditUser createdByUser;

    @Audited
    @CreatedDate
    @Column(updatable = false)
    @DiffIgnore
    private Instant createdDate;

    @Audited(targetAuditMode = NOT_AUDITED)
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY")
    @DiffIgnore
    private AuditUser modifiedByUser;

    @Audited
    @LastModifiedDate
    @DiffIgnore
    private Instant modifiedDate;

    @Version                                 // optimistic locking
    @Column(nullable = false)
    @DiffIgnore
    private Long version = 0L;
}


