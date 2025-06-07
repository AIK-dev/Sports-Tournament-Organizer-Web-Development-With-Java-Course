package com.tournament_organizer.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.tournament_organizer.service.MatchService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.entity.Match;

@RestController
@RequestMapping("/api/v1/matchs")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping
    public Match createMatch(@Valid @RequestBody Match match) {
        return matchService.save(match);
    }

    @GetMapping
    public List<Match> getAllMatchs() {
        return matchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity < Match > getMatchById(@PathVariable(value = "id") Long matchId) throws ResourceNotFoundException {
        Match match = matchService.findById(matchId);
        if (match != null) {
            return ResponseEntity.ok().body(match);
        } else {
            throw new ResourceNotFoundException("Match not found for the following id:" + matchId);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity < Match > updateMatch(@PathVariable(value = "id") Long matchId,
                                                          @Valid @RequestBody Match matchDetails) throws ResourceNotFoundException {
        Match match = matchService.findById(matchId);
        if (match != null) {
            match.copyMatch(matchDetails);

            final Match updatedMatch = matchService.save(match);
            return ResponseEntity.ok(updatedMatch);
        } else {
            throw new ResourceNotFoundException("Match not found for the following id:" + matchId);
        }
    }

    @DeleteMapping("/{id}")
    public Map < String, Boolean > deleteMatch(@PathVariable(value = "id") Long matchId)
            throws ResourceNotFoundException {

        Match match = matchService.findById(matchId);

        if (match != null) {
            matchService.delete(match);
            Map < String, Boolean > response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } else {
            throw new ResourceNotFoundException("Match not found for the following id:" + matchId);
        }
    }

}