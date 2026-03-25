package com.reza.events.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.util.Set;

@Entity
@Audited
@Table(name = "EM_EVENT_SPONSOR")
public class EventOrganiser extends AbstractAuditableEntity {

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR2(36)", updatable = false, nullable = false, unique = true)
    @Id
    private String id;                       // UUID string — not a numeric sequence

    // ── Parent ────────────────────────────────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = false)
    @DiffIgnore
    private Event event;

    // ── Identity ─────────────────────────────────────────────────────────
    private String firstName;
    private String lastName;
    private String preferredName;
    private String email;
    private String description;              // role description / company blurb

    // ── Quota management ─────────────────────────────────────────────────
    private Long quota;                      // registrations this organiser can invite
    private Long quotaModified;              // actual used/allocated count
    private Long waitlistQuota;

    private String orderNumber;              // external ordering reference

    // ── Roles ────────────────────────────────────────────────────────────
//    @OneToMany(mappedBy = "eventOrganiser", cascade = CascadeType.ALL, orphanRemoval = true)
//    @DiffIgnore @NotAudited
//    private Set<EventOrganiserRole> organiserRoles;

    // ── Registrations they manage ─────────────────────────────────────────
//    @OneToMany(mappedBy = "eventOrganiser", cascade = CascadeType.ALL)
//    @DiffIgnore @NotAudited
//    private Set<EventRegistration> managedRegistrations;
}


