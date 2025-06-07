package com.tournament_organizer.service;

import com.tournament_organizer.entity.Match;
import com.tournament_organizer.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    @Autowired
    MatchRepository matchRepository;

    public Match save(Match match) {
        return matchRepository.save(match);
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match findById(long matchId) {
        return matchRepository.findById(matchId).orElse(null);
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public void deleteById(long matchId) {
        matchRepository.deleteById(matchId);
    }
}
