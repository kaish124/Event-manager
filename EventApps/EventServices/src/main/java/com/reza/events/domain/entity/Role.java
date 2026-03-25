package com.reza.events.domain.entity;

import com.reza.events.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "EM_ROLE")
@SequenceGenerator(name = AbstractEntity.ENTITY_SEQUENCE_GEN_NAME, sequenceName = "EM_ROLE_ID_SEQ", allocationSize = 1)
public class Role extends AbstractAuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserRoleType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && type == role.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}