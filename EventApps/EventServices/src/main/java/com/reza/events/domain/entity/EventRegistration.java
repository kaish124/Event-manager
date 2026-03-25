package com.reza.events.domain.entity;

import com.reza.events.domain.hibernate.converter.YesOrNoTypeConverter;
import com.reza.events.enums.AttendeeStatus;
import com.reza.events.enums.CheckinSource;
import com.reza.events.enums.InviteSource;
import com.reza.events.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.time.Instant;
import java.util.Set;

@Entity
@Audited
@Getter
@Setter
@Builder
@Table(name = "EM_EVENT_REGISTRATION")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME,
                   sequenceName = "EM_EVENT_RGTN_ID_SEQ", allocationSize = 1)
public class EventRegistration extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITY_SEQUENCE_GEN_NAME)
    private Long id;

    // ── Parent ────────────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    @DiffIgnore
    private Event event;

    // ── Attendee Identity ─────────────────────────────────────────────────
    private String dsPersonId;               // Apple DS identity — null for external attendees
    private String firstName;
    private String lastName;
    private String email;                    // primary contact email
    private String phone;
    private String jobTitle;
    private String companyName;

    // ── Status ────────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    private AttendeeStatus status;           // REGISTERED, WAITLISTED, CANCELLED, ATTENDED, NO_SHOW

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus; // PENDING, CONFIRMED, REJECTED

    // ── Checkin ───────────────────────────────────────────────────────────
    @Column(name = "CHECKIN_DATE")
    private Instant checkinDate;

    @Enumerated(EnumType.STRING)
    private CheckinSource checkinSource;     // MANUAL, KIOSK, BADGE_SCAN, QR_CODE

    // ── Timing ───────────────────────────────────────────────────────────
    private Instant regTime;                 // when they registered

    // ── Metadata ─────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    private InviteSource inviteSource;       // SELF, ORGANISER, BULK_IMPORT

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean walkedIn;

    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean personAddedManually;

    private String registrationComments;
    private String reasonForStatusChange;

    // ── Organiser link ────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ORGANISER_ID")
    @DiffIgnore
    private EventOrganiser eventOrganiser;   // which organiser "owns" this registration

    // ── Session registrations (child collection) ──────────────────────────
    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate ASC")
    @DiffIgnore @NotAudited
    private Set<RegistrationSession> registrationSessions;

    // ── Custom field answers ───────────────────────────────────────────────
//    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
//    @DiffIgnore @NotAudited
//    private Set<RegistrationFieldValue> fieldValues;
}


