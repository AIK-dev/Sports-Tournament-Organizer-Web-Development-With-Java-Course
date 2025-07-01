package com.tournament_organizer.entity;

import com.tournament_organizer.enums.Result;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_to_play")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournamentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant1_id")
    private Participation participant1Id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant2_id")
    private Participation participant2Id;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledStart;

    @Enumerated(EnumType.STRING)
    private Result result;

}
