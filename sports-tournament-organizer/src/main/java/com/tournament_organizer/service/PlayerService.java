package com.tournament_organizer.service;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.PlayerMapper;
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
    private final PlayerMapper playerMapper;


    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
    }

    @Transactional
    public PlayerOutDTO save(PlayerInDTO playerInDTO) {
        Player playerEntity = playerMapper.toEntity(playerInDTO);
        return  playerMapper.toDto(playerRepository.save(playerEntity));
    }

    public List<PlayerOutDTO> findAll() {
        return playerRepository.findAll().stream().map(playerMapper::toDto).toList();
    }

    public Player findById(Long playerId)  {
        return playerRepository.findById(playerId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Player %s", playerId)));
    }

    @Transactional
    public PlayerOutDTO update(Long id, PlayerInDTO playerInDTO)  {
        Player playerEntity = findById(id);
        playerMapper.updateEntity(playerEntity, playerInDTO);
        return playerMapper.toDto(playerRepository.save(playerEntity));
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
