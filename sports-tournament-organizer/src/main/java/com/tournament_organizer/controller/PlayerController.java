package com.tournament_organizer.controller;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PlayerOutDTO>createPlayer(@Valid @RequestBody PlayerInDTO dto) {
        PlayerOutDTO created = playerService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<PlayerOutDTO> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public PlayerOutDTO getPlayerById(@PathVariable Long id)  {
        return playerService.findById(id);
    }
    @PreAuthorize("""
        hasRole('ADMIN') ||
        (hasRole('ORGANIZER')  && @authz.isPlayerOwner(#id)) ||
        (hasRole('PARTICIPANT') && @authz.isPlayerSelf(#id))
   
    """)
    @PutMapping("/{id}")
    public ResponseEntity<PlayerOutDTO> updatePlayer(@PathVariable Long id,
                                                      @Valid @RequestBody PlayerInDTO playerDetails) {
        PlayerOutDTO player = playerService.update(id, playerDetails);
        return ResponseEntity.ok(player);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{playerId}/owner/{userId}")
    public PlayerOutDTO changeOwner(@PathVariable Long playerId,
                                  @PathVariable Integer userId) {
        return playerService.reassignOwner(playerId, userId);
    }

    @PreAuthorize("""
    hasRole('ADMIN') ||
    (hasRole('ORGANIZER') && @authz.isTeamOwner(#teamId))
""")
    @PostMapping("/{playerId}/team/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToTeam(@PathVariable Long playerId,
                          @PathVariable Long teamId)  {
        //TODO: Make sure to check whether the adding to team operation went through correctly and supply the appropriate response
        // with HTTP.SUCCESSFUL if the operation went through or a failure message if the operation didn't succeed.
        playerService.assignToTeam(playerId, teamId);
    }
    @PreAuthorize("""
    hasRole('ADMIN') ||
    (hasRole('ORGANIZER') && @authz.isTeamOwnerFromPlayer(#playerId))
""")
    @DeleteMapping("/{playerId}/team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromTeam(@PathVariable Long playerId)  {
        playerService.removeFromTeam(playerId);
    }
    @GetMapping("/team/{teamId}")
    public List<PlayerOutDTO> roster(@PathVariable Long teamId) {
        return playerService.roster(teamId);
    }

}
