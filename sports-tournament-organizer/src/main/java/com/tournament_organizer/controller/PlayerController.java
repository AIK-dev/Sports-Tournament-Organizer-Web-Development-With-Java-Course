package com.tournament_organizer.controller;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity< PlayerOutDTO >createPlayer(@Valid @RequestBody PlayerInDTO playerInDTO) {
        PlayerOutDTO created = playerService.save(playerInDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<PlayerOutDTO> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id)  {
        return playerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity <PlayerOutDTO> updatePlayer(@PathVariable(value = "id") Long playerId,
                                                      @Valid @RequestBody PlayerInDTO playerInDTO) {
        PlayerOutDTO playerOutDTO = playerService.update(playerId, playerInDTO);
        return ResponseEntity.ok(playerOutDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
    }

    @PostMapping("/{playerId}/team/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToTeam(@PathVariable Long playerId,
                          @PathVariable Long teamId)  {
        //TODO: Make sure to check whether the adding to team operation went through correctly and supply the appropriate response
        // with HTTP.SUCCESSFUL if the operation went through or a failure message if the operation didn't succeed.
        playerService.assignToTeam(playerId, teamId);
    }

    @DeleteMapping("/{playerId}/team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromTeam(@PathVariable Long playerId)  {
        playerService.removeFromTeam(playerId);
    }
    @GetMapping("/team/{teamId}")
    public List<Player> roster(@PathVariable Long teamId) {
        return playerService.roster(teamId);
    }

}
