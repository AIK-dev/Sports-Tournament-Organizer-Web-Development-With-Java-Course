package com.tournament_organizer.repository;


import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByTournamentAndPlayer(Tournament tournament, Player player);
    boolean existsByTournamentAndTeam(Tournament tournament, Team team);

}
