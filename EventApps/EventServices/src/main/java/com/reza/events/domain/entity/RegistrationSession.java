package com.reza.events.domain.entity;

import com.reza.events.enums.AttendeeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;

// EM_REG_SESSION — bridge between a registration and a session
@Entity
@Getter
@Setter
@Table(name = "EM_REG_SESSION")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME,
        sequenceName = "EM_REG_SESSN_ID_SEQ", allocationSize = 1)
public class RegistrationSession extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITY_SEQUENCE_GEN_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGISTRATION_ID")
    @DiffIgnore
    private EventRegistration registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    @DiffIgnore
    private EventSession eventSession;

    @Enumerated(EnumType.STRING)
    private AttendeeStatus status;
}


