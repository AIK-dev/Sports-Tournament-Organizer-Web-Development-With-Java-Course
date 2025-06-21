package com.tournament_organizer.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.service.TournamentService;
import com.tournament_organizer.web.HeaderUtil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.entity.Tournament;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    public final String BASE_API_PATH = "/api/v1/tournaments";
    public final String ENTITY_NAME = "Tournament";

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.findAll();
    }

    @PostMapping
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) throws URISyntaxException {
        Tournament createdTournament = tournamentService.save(tournament);

        return ResponseEntity.created(new URI(BASE_API_PATH + createdTournament.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, createdTournament.getId().toString()))
                .body(createdTournament);
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
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedTournament.getId().toString()))
                    .body(updatedTournament);

        } else {
            throw new ResourceNotFoundException("Tournament not found for the following id:" + tournamentId);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable(value = "id") Long tournamentId)
            throws ResourceNotFoundException {

        Tournament tournament = tournamentService.findById(tournamentId);
        if (tournament != null) {
            tournamentService.delete(tournament);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tournamentId.toString())).build();

        } else {
            throw new ResourceNotFoundException("Tournament not found for the following id:" + tournamentId);
        }
    }

}