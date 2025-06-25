package com.tournament_organizer.service;

import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findById(Long playerId)  {
        return playerRepository.findById(playerId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Player %s", playerId)));
    }
    public Player update(Long id, Player patch)  {
        Player p = findById(id);
        p.setFirstName(patch.getFirstName());
        p.setSecondName(patch.getSecondName());
        p.setAge(patch.getAge());
        p.setGender(patch.getGender());
        p.setLevel(patch.getLevel());
        return p;
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public void deleteById(Long id)  {
        Player player = findById(id);
        deletePlayer(player);
    }

    @Transactional
    public void assignToTeam(Long playerId, Long teamId)  {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Player %s", playerId)));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team"));
        //TODO add check if the player and the team are the same level and gender but if the
        // team is of type MIXED it will be allowed
        player.setTeam(team);
    }
    @Transactional
    public void removeFromTeam(Long playerId)  {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player"));
        player.setTeam(null);
    }
    public List<Player> roster(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }
}

