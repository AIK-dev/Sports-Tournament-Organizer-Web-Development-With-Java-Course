package com.tournament_organizer.service;

import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.repository.TournamentRepository;
import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {
    @Autowired
    TournamentRepository tournamentRepository;

    public Tournament save(Tournament tournament) {
        Tournament createdTournament = tournamentRepository.save(tournament);
        System.out.println("We just managed to create a tournament with venues: " + tournament.getVenues().toString());
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    public Tournament findById(long tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null);
    }

    public void delete(Tournament tournament) {
        tournamentRepository.delete(tournament);
    }

    public void deleteById(long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }
}
