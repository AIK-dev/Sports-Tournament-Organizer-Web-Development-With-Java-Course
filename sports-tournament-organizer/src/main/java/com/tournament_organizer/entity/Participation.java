package com.tournament_organizer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;


@Entity
@Table(name = "participation",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_player_tournament",
                        columnNames = {"player_id", "tournament_id"}),
                @UniqueConstraint(name = "uq_team_tournament",
                        columnNames = {"team_id", "tournament_id"})
        })
@Check(constraints = "(player_id IS NOT NULL) <> (team_id IS NOT NULL)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long participantId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
}
