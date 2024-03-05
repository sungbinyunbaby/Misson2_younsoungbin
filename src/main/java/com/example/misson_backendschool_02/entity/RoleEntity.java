package com.example.misson_backendschool_02.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
