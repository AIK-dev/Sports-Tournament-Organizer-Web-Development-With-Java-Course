package com.tournament_organizer.controller;

import com.tournament_organizer.dto.match.MatchInDTO;
import com.tournament_organizer.dto.match.MatchOutDTO;
import com.tournament_organizer.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.status(HttpStatus.FOUND).body(matchService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity <MatchOutDTO> updateMatch(@PathVariable Long id,
                                                          @Valid @RequestBody MatchInDTO matchDetails) {
        return ResponseEntity.status(HttpStatus.FOUND).body(matchService.update(id, matchDetails));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        matchService.deleteById(id);
    }
}
