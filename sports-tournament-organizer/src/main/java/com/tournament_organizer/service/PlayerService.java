package com.tournament_organizer.service;


import com.tournament_organizer.entity.Player;
import com.tournament_organizer.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findById(Long playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }
}

