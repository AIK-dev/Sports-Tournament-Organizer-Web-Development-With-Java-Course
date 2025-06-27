package com.tournament_organizer.service;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.enums.Gender;
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
        if(player.getSport() != team.getSport()){
            throw new IllegalStateException(String.format("Player with sport %s does not match team sport %s",
                    player.getSport(), team.getSport()));
        }
        if (player.getLevel() != team.getAgeGroup()) {
            throw new IllegalStateException(String.format("Player age group %s does not match team age group %s",
                    player.getLevel(), team.getAgeGroup()));
        }
        switch (team.getType()) {
            case MIXED:
                break;
            case MALE:
                if (player.getGender() != Gender.MALE) {
                    throw new IllegalStateException("Team is male-only");
                }
                break;
            case FEMALE:
                if (player.getGender() != Gender.FEMALE) {
                    throw new IllegalStateException("Team is female-only");
                }
                break;
            default:
                throw new IllegalStateException("Unsupported team type: " + team.getType());
        }
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
