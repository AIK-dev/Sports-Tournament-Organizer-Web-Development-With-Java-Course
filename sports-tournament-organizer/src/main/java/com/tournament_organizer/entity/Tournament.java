package com.tournament_organizer.entity;

import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String sport;
    private int participationLimit;
    private Date startDate;
    private Date endDate;
    @OneToMany
    private ArrayList<Venue> venues;
    private String rules;

    public void copyTournament(Tournament tournament) {
        this.setName(tournament.name);
        this.setSport(tournament.sport);
        this.setParticipationLimit(tournament.participationLimit);
        this.setStartDate(tournament.startDate);
        this.setEndDate(tournament.endDate);
        this.setVenues(tournament.venues);
        this.setRules(tournament.rules);
    }
}
