package com.tournament_organizer.service;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.enums.Gender;
import com.tournament_organizer.enums.Role;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.PlayerMapper;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import com.tournament_organizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;
    private final UserRepository userRepository;
    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMapper playerMapper, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
        this.userRepository = userRepository;
    }

    public PlayerOutDTO save(PlayerInDTO dto) {
        Player player = playerMapper.toEntity(dto);
        player.setOwner(currentUser());
        return playerMapper.toDto(playerRepository.save(player));
    }

    public List<PlayerOutDTO> findAll() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toDto)
                .toList();
    }

    public PlayerOutDTO findById(Long playerId)  {
         Player found = playerRepository.findById(playerId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Player %s", playerId)));
        return playerMapper.toDto(found);
    }
    @Transactional
    public PlayerOutDTO update(Long id, PlayerInDTO dto)  {
        Player p = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Player with id %d not found",
                        id)));
        playerMapper.updateEntity(p, dto);
        return playerMapper.toDto(p);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public void deleteById(Long id)  {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player " + id + " not found");
        }
        playerRepository.deleteById(id);
    }

    @Transactional
    public void assignToTeam(Long playerId, Long teamId)  {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Player %s", playerId)));
        if (player.getTeam() != null && player.getTeam().getId() != teamId){
            throw new IllegalStateException("Player already assigned to another team");
        }
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team"));
        validateSport(player, team);
        validateAgeGroup(player, team);
        validateGender(player, team);
        player.setTeam(team);
    }
    @Transactional
    public PlayerOutDTO reassignOwner(Long playerId, Integer newOwnerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player " + playerId));
        User newOwner = userRepository.findById(newOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + newOwnerId));
        if (newOwner.getRole() != Role.ORGANIZER && newOwner.getRole() != Role.ADMIN)
            throw new IllegalStateException("New owner must be ORGANIZER or ADMIN");
        player.setOwner(newOwner);
        return playerMapper.toDto(player);
    }
    @Transactional
    public void removeFromTeam(Long playerId)  {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player"));
        player.setTeam(null);
    }
    public List<PlayerOutDTO> roster(Long teamId) {
        return playerRepository.findByTeamId(teamId).stream()
                .map(playerMapper::toDto)
                .toList();
    }
    private void validateSport(Player p, Team t) {
        if (p.getSport() != t.getSport()) {
            throw new IllegalStateException(String.format(
                    "Player sport %s does not match team sport %s", p.getSport(), t.getSport()));
        }
    }
    private void validateAgeGroup(Player p, Team t) {
        if (p.getLevel() != t.getAgeGroup()) {
            throw new IllegalStateException(String.format(
                    "Player age group %s does not match team age group %s", p.getLevel(), t.getAgeGroup()));
        }
    }
    private void validateGender(Player p, Team t) {
        switch (t.getType()) {
            case MIXED -> {}
            case MALE -> require(p.getGender() == Gender.MALE,   "Team is male-only");
            case FEMALE -> require(p.getGender() == Gender.FEMALE, "Team is female-only");
            default -> throw new IllegalStateException("Unsupported team type " + t.getType());
        }
    }
    private void require(boolean condition, String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }
    private User currentUser() {
        String uname = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(uname).orElseThrow();
    }
}

