package com.tournament_organizer.controller;

import com.tournament_organizer.dto.tournament.TournamentInDTO;
import com.tournament_organizer.dto.tournament.TournamentOutDTO;
import com.tournament_organizer.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<TournamentOutDTO> getAllTournaments() {
        return tournamentService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity <TournamentOutDTO> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.findById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TournamentOutDTO> createTournament(@Valid @RequestBody TournamentInDTO dto) /*throws URISyntaxException*/ {
        TournamentOutDTO out = tournamentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(out);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity <TournamentOutDTO> updateTournament(@PathVariable Long id,
                                                  @Valid @RequestBody TournamentInDTO dto)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentService.update(id, dto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tournamentService.deleteById(id);
    }

}
