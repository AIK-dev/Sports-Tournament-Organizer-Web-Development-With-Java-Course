package com.tournament_organizer.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.tournament_organizer.service.PlayerService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.entity.Player;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public Player createPlayer(@Valid @RequestBody Player Player) {
        return playerService.save(Player);
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity < Player > getPlayerById(@PathVariable Long playerId) throws ResourceNotFoundException {
        Player player = playerService.findById(playerId);
        if (player != null) {
            return ResponseEntity.ok().body(player);
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + playerId);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity < Player > updatePlayer(@PathVariable(value = "id") Long playerId,
                                                      @Valid @RequestBody Player playerDetails) throws ResourceNotFoundException {
        Player player = playerService.findById(playerId);
        if (player != null) {
            player.setName(playerDetails.getName());
            player.setAge(playerDetails.getAge());
            player.setGender(playerDetails.getGender());

            final Player updatedPlayer = playerService.save(player);
            return ResponseEntity.ok(updatedPlayer);
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + playerId);
        }
    }

    @DeleteMapping("/{id}")
    public Map < String, Boolean > deletePlayer(@PathVariable(value = "id") Long playerId)
            throws ResourceNotFoundException {

        Player player = playerService.findById(playerId);

        if (player != null) {
            playerService.deletePlayer(player);
            Map < String, Boolean > response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } else {
            throw new ResourceNotFoundException("Player not found for the following id:" + playerId);
        }
    }

}