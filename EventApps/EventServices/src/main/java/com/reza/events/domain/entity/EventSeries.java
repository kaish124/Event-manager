package com.reza.events.domain.entity;

import com.reza.events.domain.hibernate.converter.YesOrNoTypeConverter;
import com.reza.events.enums.EventCategory;
import com.reza.events.enums.EventType;
import com.reza.events.enums.SeriesStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "EM_EVENT")
public class EventSeries extends AbstractAuditableEntity {

    @Id
    @Column(name = "ID")
    private Long eventId;

    private String name;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    private String campaignCode;

    private Instant startDate;
    private Instant endDate;
    private ZoneId timeZone;

    @Enumerated(EnumType.STRING)
    private SeriesStatus seriesStatus;       // ACTIVE, ARCHIVED, CANCELLED

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean confidential;

    @Column(name = "REGISTRATION_LIMIT")
    private Integer registrationLimit;

    // Child events — fetched lazily; used only for admin summaries
    @OneToMany
    @JoinColumn(name = "SERIES_ID")
    @DiffIgnore
    private Set<Event> childEvents;

    @Transient
    private Long childEventCount;
}


