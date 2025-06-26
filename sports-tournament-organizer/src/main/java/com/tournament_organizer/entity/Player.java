package com.tournament_organizer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tournament_organizer.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('male','female')")
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonBackReference
    private User user;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Player() {
    }

}
