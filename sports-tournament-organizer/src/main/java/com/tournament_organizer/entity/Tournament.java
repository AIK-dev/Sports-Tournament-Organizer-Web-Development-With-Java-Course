package com.tournament_organizer.entity;

import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.enums.Sport;
import com.tournament_organizer.enums.TeamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//TODO add also a level here professional can't play against amateurs and gender too this is for the participation
// logic that logic will be used in the participation mapper and there we should add also logic for sport like
// if the proper team sport can play in this tournament and use the logic to check for limits
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Sport sport;
    private int participationLimit;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations  = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawType drawType;

    @ManyToMany
    @JoinTable(name = "tournament_venue",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "venue_id"))
    private List<Venue> venues = new ArrayList<>();
    private String rules;
    @Enumerated(EnumType.STRING)
    private AgeGroup level;
    @Enumerated(EnumType.STRING)
    private TeamType category;


}
