package com.tournament_organizer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.enums.Sport;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


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

    /*@JsonManagedReference*/
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

    /*public void copyTournament(Tournament tournament) {
        this.setName(tournament.name);
        this.setSport(tournament.sport);
        this.setParticipationLimit(tournament.participationLimit);
        this.setStartDate(tournament.startDate);
        this.setEndDate(tournament.endDate);
        this.setVenues(tournament.venues);
        this.setRules(tournament.rules);
    }

    public void addParticipation(Participation participation) {
        this.participations.add(participation);
        participation.setTournament(this);
    }

    public void removeParticipation(Participation participation) {
        this.participations.remove(participation);
        participation.setTournament(null);
    }*/
}
