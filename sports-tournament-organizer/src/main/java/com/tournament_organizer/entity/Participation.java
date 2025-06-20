package com.tournament_organizer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
public class Participation implements Serializable {
    @Id
    private long participantId;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Player player;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tournament_participation",
            joinColumns = @JoinColumn(name = "participation_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private Tournament tournament;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    @Column(name = "team", length = 1024)
    public Team getTeam() {
        return team;
    }

    @Column(name = "player", length = 1024)
    public Player getPlayer() {
        return player;
    }

    @Column(name = "tournament", length = 4096)
    public Tournament getTournament() {
        return tournament;
    }


    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTeam(Team team) {
        this.team = team;
    }




}
