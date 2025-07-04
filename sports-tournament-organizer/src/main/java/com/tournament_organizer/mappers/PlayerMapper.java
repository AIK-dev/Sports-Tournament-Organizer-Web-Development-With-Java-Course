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
    public Player toEntity(PlayerInDTO playerInDTO) {
        Player p = new Player();
        copyPrimitives(playerInDTO, p);
        p.setTeam(resolveTeam(playerInDTO.getTeamId()));
        return p;
    }

    public void updateEntity(Player currentEntity, PlayerInDTO playerInDTO) {
        copyPrimitives(playerInDTO, currentEntity);
        currentEntity.setTeam(resolveTeam(playerInDTO.getTeamId()));
    }

    public PlayerOutDTO toDto(Player p) {
        PlayerOutDTO playerOutDTO = new PlayerOutDTO();
        playerOutDTO.setId(p.getId());
        copyDtoPrimitives(p, playerOutDTO);
        playerOutDTO.setAssociatedTeam(p.getTeam() != null ? p.getTeam().getName() : "SINGLE PLAYER");
        playerOutDTO.setUserId(p.getUser() != null ? p.getUser().getId() : null);
        playerOutDTO.setOwnerId(p.getOwner() != null ? p.getOwner().getId() : null);
        return playerOutDTO;
    }
    private void copyPrimitives(PlayerInDTO src, Player target) {
        target.setFirstName(src.getFirstName());
        target.setSecondName(src.getSecondName());
        target.setAge(src.getAge());
        target.setGender(src.getGender());
        target.setLevel(src.getLevel());
        target.setSport(src.getSport());
    }
    private void copyDtoPrimitives(Player src, PlayerOutDTO target) {
        target.setFirstName(src.getFirstName());
        target.setSecondName(src.getSecondName());
        target.setAge(src.getAge());
        target.setGender(src.getGender());
        target.setLevel(src.getLevel());
        target.setSport(src.getSport());
    }
    private Team resolveTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + teamId + " not found"));
    }
}