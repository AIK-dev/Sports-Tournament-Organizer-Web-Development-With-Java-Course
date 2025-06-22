package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.dto.participation.ParticipationDisplayDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import com.tournament_organizer.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipationMapper {
    private final PlayerRepository playerRepo;
    private final TeamRepository teamRepo;
    private final TournamentRepository tournamentRepo;
    public Participation toEntity(ParticipationCreationDTO dto) {
        Participation participation = new Participation();
        Tournament tournament = tournamentRepo.findById(dto.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + dto.getTournamentId()));
        validateDrawType(tournament.getDrawType(), dto.getPlayerId(), dto.getTeamId());

        participation.setTournament(tournament);

        if (dto.getPlayerId() != null) {
            participation.setPlayer(playerRepo.findById(dto.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + dto.getPlayerId())));
        }
        if (dto.getTeamId() != null) {
            participation.setTeam(teamRepo.findById(dto.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + dto.getTeamId())));
        }
        return participation;
    }
    public void updateEntity(Participation entity, ParticipationCreationDTO patch) {
        Tournament tournament = tournamentRepo.findById(patch.getTournamentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tournament " + patch.getTournamentId() + " not found"));

        validateDrawType(tournament.getDrawType(), patch.getPlayerId(), patch.getTeamId());

        entity.setTournament(tournament);

        if (patch.getPlayerId() != null) {
            entity.setPlayer(playerRepo.findById(patch.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player " + patch.getPlayerId() + " not found")));

            entity.setTeam(null);
        } else if (patch.getTeamId() != null) {
            entity.setTeam(teamRepo.findById(patch.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team " + patch.getTeamId() + " not found")));
            entity.setPlayer(null);
        }
    }
    public ParticipationDisplayDTO toDto(Participation participation) {
        ParticipationDisplayDTO dto = new ParticipationDisplayDTO();
        dto.setParticipantId(participation.getParticipantId());
        dto.setTournamentId(participation.getTournament().getId());
        if (participation.getPlayer() != null){
            dto.setPlayerId(participation.getPlayer().getId());
        }
        if (participation.getTeam() != null){
            dto.setTeamId(participation.getTeam().getId());
        }
        return dto;
    }
    private void validateDrawType(DrawType draw, Long playerId, Long teamId) {
        if (draw == DrawType.SINGLES && teamId != null)
            throw new IllegalStateException("Singles tournament accepts players only");
        if (draw == DrawType.TEAMS   && playerId != null)
            throw new IllegalStateException("Team tournament accepts teams only");
        if ((playerId == null) == (teamId == null))  // XOR test
            throw new IllegalStateException("Exactly one of playerId or teamId must be supplied");
    }
}
