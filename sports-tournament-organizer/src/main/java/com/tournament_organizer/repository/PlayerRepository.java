package com.tournament_organizer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tournament_organizer.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
}
