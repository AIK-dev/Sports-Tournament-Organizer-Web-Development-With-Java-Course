package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Result result;

    public void copyMatch(Match match) {
        this.setTournamentId(match.tournamentId);
        this.setParticipant1Id(match.participant1Id);
        this.setParticipant2Id(match.participant2Id);
        this.setVenueId(match.venueId);
        this.setScheduledStart(match.scheduledStart);
        this.setResult(match.result);
    }
}
