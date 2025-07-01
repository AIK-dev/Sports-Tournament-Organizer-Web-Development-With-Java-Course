package com.tournament_organizer.controller;

import com.tournament_organizer.dto.team.TeamInDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @PostMapping
    public ResponseEntity<TeamOutDTO> createTeam(@Valid @RequestBody TeamInDTO team) {
        TeamOutDTO created = teamService.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<TeamOutDTO> getAllTeams() {
        return teamService.findAll();
    }
    @PreAuthorize("""
        hasRole('ADMIN') ||
        (hasRole('ORGANIZER') && @authz.isTeamOwner(#id))
    """)
    @PutMapping("/{id}")
    public ResponseEntity<TeamOutDTO> updateTeam(@PathVariable Long id,
                                               @Valid @RequestBody TeamInDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(teamService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamOutDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.findById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{teamId}/owner/{userId}")
    public TeamOutDTO changeOwner(@PathVariable Long teamId,
                                  @PathVariable Integer userId) {
        return teamService.reassignOwner(teamId, userId);
    }
    @PreAuthorize("""
        hasRole('ADMIN') ||
        (hasRole('ORGANIZER') && @authz.isTeamOwner(#id))
    """)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }

}
