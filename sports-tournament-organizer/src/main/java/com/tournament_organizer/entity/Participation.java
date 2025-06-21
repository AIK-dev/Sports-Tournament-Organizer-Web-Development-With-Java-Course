package com.tournament_organizer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Entity
@Table(name = "participation")
public class Participation implements Serializable {
    @Id
    private long participantId;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Player player;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tournament_participation",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private Tournament tournament;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getParticipantId() {
        return participantId;
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
}
