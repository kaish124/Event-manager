package com.reza.events.domain.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "CREATED_BY", updatable = false)
    private User createdByUser;

    @CreatedDate
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "MODIFIED_BY")
    private User modifiedByUser;

    @LastModifiedDate
    private Instant modifiedDate;
}
