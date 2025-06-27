package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.player.PlayerInDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PlayerMapper {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public Player toEntity(PlayerInDTO playerInDTO) {
        Player p = new Player();
        p.setFirstName(playerInDTO.getFirstName());
        p.setSecondName(playerInDTO.getSecondName());
        p.setAge(playerInDTO.getAge());
        p.setLevel(playerInDTO.getLevel());


        if (playerInDTO.getTeamId() != null) {
            Team team = teamRepository.findById(playerInDTO.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team " + playerInDTO.getTeamId() + " not found"));
            p.setTeam(team);
        }

        return p;
    }

    public void updateEntity(Player currentEntity, PlayerInDTO playerInDTO) {

        // Just don't touch the player id.
        if (playerInDTO.getTeamId() != null) {
            Team team = teamRepository.findById(playerInDTO.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team " + playerInDTO.getTeamId() + " not found"));
            currentEntity.setTeam(team);
        } else {
            // TODO: Decide what we do if we are passing in a null for the team.
            //  should we enable the functionality for unassigning a team from a player?
            currentEntity.setTeam(null);
        }

        currentEntity.setFirstName(playerInDTO.getFirstName());
        currentEntity.setFirstName(playerInDTO.getSecondName());
        currentEntity.setGender(playerInDTO.getGender());
        currentEntity.setAge(playerInDTO.getAge());
    }

    public PlayerOutDTO toDto(Player p) {
        PlayerOutDTO playerOutDTO = new PlayerOutDTO();

        playerOutDTO.setId(p.getId());
        playerOutDTO.setFirstName(p.getFirstName());
        playerOutDTO.setSecondName(p.getSecondName());
        playerOutDTO.setGender(p.getGender());
        playerOutDTO.setAge(p.getAge());
        playerOutDTO.setLevel(p.getLevel());
        if (p.getTeam() != null) {
            playerOutDTO.setAssociatedTeam(p.getTeam().getName());
        } else {
            playerOutDTO.setAssociatedTeam("SINGLE PLAYER");
        }
        return playerOutDTO;
    }
}
