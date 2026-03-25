package com.reza.events.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "EM_USER")
public class User extends AbstractAuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String preferredName;

    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;

    // Convenience method to add a user role
    public void addUserRole(UserRole userRole) {
        if (userRoles == null) {
            userRoles = new java.util.HashSet<>();
        }
        userRoles.add(userRole);
        userRole.setUser(this);
    }

    // Convenience method to remove a user role
    public void removeUserRole(UserRole userRole) {
        if (userRoles != null) {
            userRoles.remove(userRole);
            userRole.setUser(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}