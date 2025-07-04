package com.tournament_organizer.controller;

import com.tournament_organizer.dto.match.MatchInDTO;
import com.tournament_organizer.dto.match.MatchOutDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MatchOutDTO> createMatch(@Valid @RequestBody MatchInDTO match) {
        MatchOutDTO created = matchService.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<MatchOutDTO> getAllMatches() {
        return matchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity <MatchOutDTO> getMatchById(@PathVariable Long id)  {
        return ResponseEntity.ok(matchService.findById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity <MatchOutDTO> updateMatch(@PathVariable Long id,
                                                          @Valid @RequestBody MatchInDTO matchDetails) {
        return ResponseEntity.ok(matchService.update(id, matchDetails));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        matchService.deleteById(id);
    }

    @GetMapping("/tournaments/{tournamentID}")
    public ResponseEntity<List<MatchOutDTO>> tournamentSchedule(@PathVariable Long tournamentID) {
        return ResponseEntity.ok(matchService.tournamentSchedule(tournamentID));
    }
}
