package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.dto.team.TeamInDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    private final PlayerRepository playerRepository;

    public Team toEntity(TeamInDTO dto) {
        Team team = new Team();
        team.setName(dto.getName());
        team.setAgeGroup(dto.getAgeGroup());
        team.setType(dto.getTeamType());
        team.setSport(dto.getSport());
        if (dto.getPlayerIds() != null && !dto.getPlayerIds().isEmpty()) {
            List<Player> players = dto.getPlayerIds().stream()
                    .map(id -> playerRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Player " + id + " not found")))
                    .collect(Collectors.toList());
            players.forEach(player -> player.setTeam(team));
            team.setPlayers(players);
        }
        return team;
    }

    public void updateEntity(Team team, TeamInDTO dto) {
        team.setName(dto.getName());
        team.setAgeGroup(dto.getAgeGroup());
        team.setType(dto.getTeamType());
        team.setSport(dto.getSport());
        if (dto.getPlayerIds() != null) {
            List<Player> players = dto.getPlayerIds().stream()
                    .map(id -> playerRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Player " + id + " not found")))
                    .collect(Collectors.toList());
            players.forEach(player -> player.setTeam(team));
            team.setPlayers(players);
        }
    }

    public TeamOutDTO toDto(Team team) {
        List<PlayerOutDTO> playerDtos = null;
        if (team.getPlayers() != null) {
            playerDtos = team.getPlayers().stream()
                    .map(player -> new PlayerOutDTO(
                            player.getId(),
                            player.getFirstName(),
                            player.getSecondName(),
                            player.getAge(),
                            player.getGender(),
                            player.getLevel(),
                            player.getSport(),
                            team.getName()
                    )).collect(Collectors.toList());
        }
        return new TeamOutDTO(
                team.getId(),
                team.getName(),
                team.getAgeGroup(),
                team.getType(),
                team.getSport(),
                playerDtos
        );
    }
}