package com.tournament_organizer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class Tournament implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sport;
    private int participationLimit;
    private Date startDate;
    private Date endDate;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<Participation>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tournament_venue",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "venue_id"))
    private List<Venue> venues = new ArrayList<Venue>();
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

    public void addParticipation(Participation participation) {
        this.participations.add(participation);
        participation.setTournament(this);
    }

    public void removeParticipation(Participation participation) {
        this.participations.remove(participation);
        participation.setTournament(null);
    }

    @Column(name = "venues", nullable = false, length = 1024)
    public List<Venue> getVenues() {
        return venues;
    }
    public void setVenues(List<Venue> venues) {
        this.venues = this.venues;
    }


}
