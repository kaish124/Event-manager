package com.reza.events.domain.entity;

import com.reza.events.domain.hibernate.converter.YesOrNoTypeConverter;
import com.reza.events.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;


@Entity
@Audited
@Getter
@Setter
@Table(name = "EM_EVENT")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME,
                   sequenceName = "EM_EVENT_ID_SEQ", allocationSize = 1)
public class Event extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITY_SEQUENCE_GEN_NAME)
    private Long id;

    @Column(nullable = false)
    private String name;                     // internal name (not shown to attendees)

    @Column(nullable = false)
    private String displayName;              // public-facing name

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;                  // IN_PERSON, VIRTUAL, HYBRID

    @Enumerated(EnumType.STRING)
    private EventStatus status;              // DRAFT, PUBLISHED, CANCELLED, COMPLETED

    private Instant startDate;
    private Instant endDate;
    private ZoneId timeZone;

    @Enumerated(EnumType.STRING)
    private EventCategory category;          // CONFERENCE, WORKSHOP, BRIEFING, etc.

    private String campaignCode;
    private Integer eventCode;               // human-readable short code

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean seriesIndicator;         // 'Y' when this row IS the series parent

    @Column(name = "SERIES_ID")
    private Long seriesId;                   // FK to the series Event row (null if not part of a series)

    @Column(name = "REGISTRATION_LIMIT")
    private Integer registrationLimit;       // null → unlimited

    @Enumerated(EnumType.STRING)
    private EventWaitlistBehaviour waitlistBehaviour;  // NONE, AUTO_APPROVE, MANUAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENUE_ID")
    @DiffIgnore
    private Venue venue;

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean confidential;

    @Enumerated(EnumType.STRING)
    private EventVisibility visibility;      // PUBLIC, PRIVATE, INVITE_ONLY

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @DiffIgnore @NotAudited
    private Set<EventSession> sessions;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @DiffIgnore @NotAudited
    private Set<EventOrganiser> organisers;
}

