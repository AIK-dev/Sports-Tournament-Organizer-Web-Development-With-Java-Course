package com.tournament_organizer.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.tournament_organizer.service.TournamentService;
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
import com.tournament_organizer.entity.Tournament;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping
    public Tournament createTournament(@Valid @RequestBody Tournament tournament) {
        return tournamentService.save(tournament);
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity < Tournament > getTournamentById(@PathVariable(value = "id") Long tournamentId) throws ResourceNotFoundException {
        Tournament tournament = tournamentService.findById(tournamentId);
        if (tournament != null) {
            return ResponseEntity.ok().body(tournament);
        } else {
            throw new ResourceNotFoundException("Tournament not found for the following id:" + tournamentId);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity < Tournament > updateTournament(@PathVariable(value = "id") Long tournamentId,
                                                  @Valid @RequestBody Tournament tournamentDetails) throws ResourceNotFoundException {
        Tournament tournament = tournamentService.findById(tournamentId);
        if (tournament != null) {
            tournament.copyTournament(tournamentDetails);

            final Tournament updatedTournament = tournamentService.save(tournament);
            return ResponseEntity.ok(updatedTournament);
        } else {
            throw new ResourceNotFoundException("Tournament not found for the following id:" + tournamentId);
        }
    }

    @DeleteMapping("/{id}")
    public Map < String, Boolean > deleteTournament(@PathVariable(value = "id") Long tournamentId)
            throws ResourceNotFoundException {

        Tournament tournament = tournamentService.findById(tournamentId);

        if (tournament != null) {
            tournamentService.delete(tournament);
            Map < String, Boolean > response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } else {
            throw new ResourceNotFoundException("Tournament not found for the following id:" + tournamentId);
        }
    }

}