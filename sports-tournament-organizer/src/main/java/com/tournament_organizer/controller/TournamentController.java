package com.tournament_organizer.controller;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import com.tournament_organizer.dto.tournament.TournamentInDTO;
import com.tournament_organizer.dto.tournament.TournamentOutDTO;
import com.tournament_organizer.mappers.TournamentMapper;
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

/*    public final String BASE_API_PATH = "/api/v1/tournaments";
    public final String ENTITY_NAME = "Tournament";*/

    private final TournamentService tournamentService;
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<TournamentOutDTO> getAllTournaments() {
        return tournamentService.findAll();
    }
    //TODO If we use axios in the front end because Found is with status 302 it won't see it axios accept only 200+ statuses
    @GetMapping("/{id}")
    public ResponseEntity <TournamentOutDTO> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(tournamentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TournamentOutDTO> createTournament(@Valid @RequestBody TournamentInDTO dto) /*throws URISyntaxException*/ {
        TournamentOutDTO out = tournamentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                /*.created(new URI("/api/v1/tournaments/" + out.getId()))*/
                .body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity <TournamentOutDTO> updateTournament(@PathVariable Long id,
                                                  @Valid @RequestBody TournamentInDTO dto)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tournamentService.deleteById(id);
    }

}
