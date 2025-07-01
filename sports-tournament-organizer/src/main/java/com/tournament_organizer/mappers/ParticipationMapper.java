package com.tournament_organizer.mappers;

import com.tournament_organizer.dto.participation.ParticipationInDTO;
import com.tournament_organizer.dto.participation.ParticipationOutDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.enums.Gender;
import com.tournament_organizer.enums.TeamType;
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
    public Participation toEntity(ParticipationInDTO dto) {
        Tournament tournament = tournamentRepo.findById(dto.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + dto.getTournamentId()));
        checkEligibility(tournament, dto.getPlayerId(), dto.getTeamId());
        Participation participation = new Participation();
        participation.setTournament(tournament);
        if (dto.getPlayerId() != null) {
            participation.setPlayer(playerRepo.findById(dto.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player " + dto.getPlayerId())));
        } else {                                        // teamId is guaranteed non-null here
            participation.setTeam(teamRepo.findById(dto.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team " + dto.getTeamId())));
        }
        return participation;
    }
    public void updateEntity(Participation entity, ParticipationInDTO patch) {
        Tournament tournament = tournamentRepo.findById(patch.getTournamentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tournament " + patch.getTournamentId() + " not found"));
        checkEligibility(tournament, patch.getPlayerId(), patch.getTeamId());
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
    public ParticipationOutDTO toDto(Participation participation) {
        ParticipationOutDTO dto = new ParticipationOutDTO();
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
        if ((playerId == null) == (teamId == null))
            throw new IllegalStateException("Exactly one of playerId or teamId must be supplied");
    }

    private void checkEligibilityPlayer(Tournament tournament, Long playerId) {
        Player p = playerRepo.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player " + playerId));
        if (p.getLevel() != tournament.getLevel()) {
            throw new IllegalStateException("Player level differs from tournament level");
        }
        if (tournament.getCategory() == TeamType.MALE && p.getGender() != Gender.MALE)
            throw new IllegalStateException("This is a male-only tournament");
        if (tournament.getCategory() == TeamType.FEMALE && p.getGender() != Gender.FEMALE)
            throw new IllegalStateException("This is a female-only tournament");
    }

    private void checkEligibilityTeam(Tournament tournament, Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + teamId));
        if (team.getAgeGroup() != tournament.getLevel()) {
            throw new IllegalStateException("Team level differs from tournament level");
        }
        switch (tournament.getCategory()) {
            case MIXED -> {
                if (team.getType() != TeamType.MIXED) {
                    throw new IllegalStateException(
                            "Only mixed teams can play in a mixed tournament");
                }
            }
            case MALE, FEMALE -> {
                if (team.getType() != tournament.getCategory()) {
                    throw new IllegalStateException(
                            "Team gender category must be " + tournament.getCategory());
                }
            }
        }
    }

    private void checkEligibility(Tournament tournament, Long playerId, Long teamId) {
        validateDrawType(tournament.getDrawType(), playerId, teamId);
        if (tournament.getParticipations().size() >= tournament.getParticipationLimit()) {
            throw new IllegalStateException("Participation limit reached for tournament " + tournament.getName());
        }
        if (playerId != null) {
            checkEligibilityPlayer(tournament, playerId);
        } else {
            checkEligibilityTeam(tournament, teamId);
        }
    }
}
