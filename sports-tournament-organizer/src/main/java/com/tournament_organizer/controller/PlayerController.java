package com.tournament_organizer.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.tournament_organizer.service.PlayerService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.tournament_organizer.entity.Player;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity< Player >createPlayer(@Valid @RequestBody Player Player) {
        Player created = playerService.save(Player);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id)  {
        /*Player player = playerService.findById(id);
        if (player != null) {
            return ResponseEntity.ok().body(player);
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + id);
        }*/
        return playerService.findById(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity <Player> updatePlayer(@PathVariable(value = "id") Long playerId,
                                                      @Valid @RequestBody Player playerDetails) {
        Player player = playerService.update(playerId, playerDetails);
        /*if (player != null) {
            player.setName(playerDetails.getName());
            player.setAge(playerDetails.getAge());
            player.setGender(playerDetails.getGender());

            final Player updatedPlayer = playerService.save(player);
            return ResponseEntity.ok(updatedPlayer);
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + playerId);
        }*/
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        /*if (player != null) {
            playerService.deletePlayer(player);
            Map < String, Boolean > response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + playerId);
        }*/
    }
    @PostMapping("/{playerId}/team/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToTeam(@PathVariable Long playerId,
                          @PathVariable Long teamId)  {
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
