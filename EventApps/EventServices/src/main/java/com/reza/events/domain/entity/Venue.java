package com.reza.events.domain.entity;

import com.reza.events.domain.hibernate.converter.YesOrNoTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.util.Set;

@Entity
@Audited
@Getter
@Setter
@Table(name = "EM_VENUE")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME,
                   sequenceName = "EM_VENUE_ID_SEQ", allocationSize = 1)
public class Venue extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITY_SEQUENCE_GEN_NAME)
    private Long id;

    @Column(nullable = false)
    private String name;                     // venue name

    @Column(nullable = false)
    private String displayName;              // public-facing venue name

    private String description;              // venue description

    // Address fields
    @Column(name = "ADDRESS_LINE_1")
    private String addressLine1;

    @Column(name = "ADDRESS_LINE_2")
    private String addressLine2;

    private String city;

    @Column(name = "STATE_PROVINCE")
    private String stateProvince;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    private String country;

    // Contact information
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    private String email;

    private String website;

    // Venue details
    private Integer capacity;                // maximum capacity

    @Column(name = "PARKING_AVAILABLE")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean parkingAvailable;

    @Column(name = "WHEELCHAIR_ACCESSIBLE")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean wheelchairAccessible;

    @Column(name = "WIFI_AVAILABLE")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean wifiAvailable;

    @Column(name = "CATERING_AVAILABLE")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean cateringAvailable;

    // Location coordinates (optional)
    private Double latitude;
    private Double longitude;

    // Additional information
    @Column(name = "SPECIAL_INSTRUCTIONS", length = 1000)
    private String specialInstructions;

    @Column(name = "SETUP_NOTES", length = 1000)
    private String setupNotes;

    // Status
    @Column(columnDefinition = "CHAR")
    @Convert(converter = YesOrNoTypeConverter.class)
    private boolean active = true;

    // Relationships
    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
    @DiffIgnore @NotAudited
    private Set<Event> events;
}