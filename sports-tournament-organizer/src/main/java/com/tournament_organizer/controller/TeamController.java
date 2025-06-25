package com.tournament_organizer.controller;

import com.tournament_organizer.entity.Team;
import com.tournament_organizer.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    private final TeamService teamService;
    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public Team createTeam(@Valid @RequestBody Team team) {
        return teamService.save(team);
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable(value = "id") Long teamId,
                                               @Valid @RequestBody Team teamDetails) {
        Team updatedTeam = teamService.update(teamId, teamDetails);
        return ResponseEntity.ok(updatedTeam);
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }

}
