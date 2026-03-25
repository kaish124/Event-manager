package com.reza.events.domain.entity;

import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// AuditUser.java — maps to EM_USER, used only by AbstractAuditableEntity
@Entity
@Table(name = "EM_USER")
@Getter
@Setter
public class AuditUser extends AbstractEntity {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AuditUser other)) return false;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}


