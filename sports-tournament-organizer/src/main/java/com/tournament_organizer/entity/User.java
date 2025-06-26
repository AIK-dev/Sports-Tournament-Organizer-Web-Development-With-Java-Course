package com.tournament_organizer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tournament_organizer.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "u_name", unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.VIEWER;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Player player;

    public User() {
    }

}
