package com.tournament_organizer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Participation {
    @Id
    private long participantId;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Player player;
}
