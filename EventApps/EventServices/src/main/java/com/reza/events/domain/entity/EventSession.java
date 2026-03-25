package com.reza.events.domain.entity;

import com.reza.events.domain.hibernate.converter.YesOrNoTypeConverter;
import com.reza.events.enums.EventWaitlistBehaviour;
import com.reza.events.enums.SessionCategoryType;
import com.reza.events.enums.SessionStatus;
import com.reza.events.enums.VisibilityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.time.Instant;

@Entity
@Audited
@Getter
@Setter
@Table(name = "EM_EVENT_SESSION")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME,
                   sequenceName = "EM_EVENT_SESSN_ID_SEQ", allocationSize = 1)
public class EventSession extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITY_SEQUENCE_GEN_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    @DiffIgnore
    private Event event;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;            // DRAFT, PUBLISHED, CANCELLED, COMPLETED

    @Enumerated(EnumType.STRING)
    private SessionCategoryType category;    // KEYNOTE, BREAKOUT, LAB, PANEL, etc.

    private Instant startDate;
    private Instant endDate;
    private Instant registrationCutOffDate;  // registrations close before session start

    private Integer capacity;                // null → unlimited
    private Integer waitlistCapacity;

    @Enumerated(EnumType.STRING)
    private EventWaitlistBehaviour waitlistBehaviour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENUE_ID")
    @DiffIgnore
    private Venue venue;

    private String room;                     // room number / area within the venue

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean questionsEnabled;

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean experienceRatingEnabled;

    @Enumerated(EnumType.STRING)
    private VisibilityType visibility;       // PUBLIC, PRIVATE

    @Transient
    private Long registrationCount;
}


