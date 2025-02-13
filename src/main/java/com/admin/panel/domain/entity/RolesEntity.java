package com.admin.panel.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class RolesEntity {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Default constructor
    public RolesEntity() {
    }

    // Additional constructors (if any)
    public RolesEntity(String name) {
        this.name = name;
    }

}