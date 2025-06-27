package com.tournament_organizer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.Sport;
import com.tournament_organizer.enums.TeamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "team")
public class Team  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    @Enumerated(EnumType.STRING)
    private Sport sport;

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
}
