package com.tournament_organizer.repository;

import com.tournament_organizer.entity.Participation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
