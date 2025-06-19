package com.tournament_organizer.repository;

import com.tournament_organizer.entity.Match;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
