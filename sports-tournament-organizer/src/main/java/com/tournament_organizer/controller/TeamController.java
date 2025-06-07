package com.tournament_organizer.controller;


import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping
    public Team createTeam(@Valid @RequestBody Team team) {
        return teamService.save(team);
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }


    @PostMapping("/{teamId}/players")
    public Team addPlayer(@PathVariable Long teamId, @Valid @RequestBody Player player) {
        return teamService.addPlayer(teamId, player);
    }

    @DeleteMapping("/{teamId}/players/{playerId}")
    public Team removePlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        return teamService.removePlayer(teamId, playerId);
    }
}
