package com.tournament_organizer.repository;

import com.tournament_organizer.entity.Match;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Tournament;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByTournamentIdAndParticipant1IdAndParticipant2Id(Tournament tournamentId,
                                                                   Participation participant1Id, Participation participant2Id);
}
