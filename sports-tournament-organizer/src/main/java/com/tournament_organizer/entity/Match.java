package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Tournament tournamentId;
    @ManyToOne
    private Participation participant1Id;
    @ManyToOne
    private Participation participant2Id;
    @ManyToOne
    private Venue venueId;
    private Date scheduledStart;
    private enum Result {
        WIN_P1,
        WIN_P2,
        DRAW,
        NOT_PLAYED
    }
}
