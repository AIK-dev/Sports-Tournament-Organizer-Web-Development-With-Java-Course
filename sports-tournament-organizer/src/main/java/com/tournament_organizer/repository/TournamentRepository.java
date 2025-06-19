package com.tournament_organizer.repository;

import com.tournament_organizer.entity.Tournament;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
