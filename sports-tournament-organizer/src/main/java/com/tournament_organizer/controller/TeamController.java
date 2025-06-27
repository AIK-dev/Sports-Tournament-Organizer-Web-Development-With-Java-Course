
package com.tournament_organizer.controller;

import com.tournament_organizer.dto.team.TeamInDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamOutDTO> createTeam(@Valid @RequestBody TeamInDTO dto) {
        TeamOutDTO created = teamService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<TeamOutDTO> getAllTeams() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamOutDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teamService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamOutDTO> updateTeam(@PathVariable Long id,
                                                 @Valid @RequestBody TeamInDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(teamService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}
