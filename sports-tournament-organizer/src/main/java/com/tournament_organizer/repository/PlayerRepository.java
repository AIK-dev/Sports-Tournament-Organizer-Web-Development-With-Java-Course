package com.tournament_organizer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tournament_organizer.entity.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
    List<Player> findByTeamId(Long teamId);
}
