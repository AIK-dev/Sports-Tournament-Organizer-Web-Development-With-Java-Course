package com.tournament_organizer.service;


import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.ParticipationRepository;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import com.tournament_organizer.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipationService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ParticipationRepository participationRepository;

    public Participation createParticipation(ParticipationCreationDTO participationDTO) throws ResourceNotFoundException {
        Participation newParticipation = new Participation();
        Participation resultParticipation = updateParticipationAttributes(newParticipation, participationDTO);
        return resultParticipation;
    }

    public Participation findParticipationById(Long participationId) throws ResourceNotFoundException {
        return participationRepository.findById(participationId).orElseThrow(() -> new ResourceNotFoundException(participationId.toString()));
    }

    public Participation updateParticipation(Long participationId,  ParticipationCreationDTO participationCreationDTO) throws ResourceNotFoundException {
        Participation participation = findParticipationById(participationId);
        Participation resultParticipation = updateParticipationAttributes(participation, participationCreationDTO);
        return resultParticipation;
    }

    private Participation updateParticipationAttributes(Participation participation, ParticipationCreationDTO participationDTO) throws ResourceNotFoundException {
        Long playerId = participationDTO.getPlayerId();
        Long teamId = participationDTO.getTeamId();
        Long tournamentId = participationDTO.getTournamentId();

        Tournament tournament = null;
        Player player = null;
        Team team = null;

        if (tournamentId != null) {
            Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentId);
            if (optionalTournament.isEmpty()) {
                throw new ResourceNotFoundException("A Tournament with Id: " + tournamentId + "has not been found to be linked with this participation!");
            } else {
                tournament = optionalTournament.get();
            }

        } else {
            throw new IllegalArgumentException("Participation without an associated Tournament is not allowed");
        }

        if (playerId != null) {
            Optional<Player> optionalPlayer = playerRepository.findById(playerId);
            if (optionalPlayer.isEmpty()) {
                throw new ResourceNotFoundException("A Player with Id :" + playerId + " has not been found to be added to the participation.");
            } else {
                player = optionalPlayer.get();
            }
        }

        if (teamId != null) {
            Optional<Team> optionalTeam = teamRepository.findById(teamId);
            if (optionalTeam.isEmpty()) {
                throw new ResourceNotFoundException("A Team with Id :" + teamId + " has not been found to be added to the participation ");
            } else {
                team = optionalTeam.get();
            }
        }

        participation.setPlayer(player);
        participation.setTeam(team);
        participation.setTournament(tournament);

        Participation resultParticipation = participationRepository.save(participation);

        // Note: this is done, because we are updating a given participation, we are only updating the information on the participation side, and we do not need to edit its corresponding tournament as well,
        // so the below update is unnecessary and will cause a hibernate duplicate key error.

        // The current fix is to introduce a boolean to the Participation CreationDTO which points out and use that to distinguish whether the current
        // update is done as the result of a Create (via HTTP Post) or Update (via HTTP Patch) operation on the participation entity.
        // In the case where the Create operation is carried out, the boolean does not need to be used.

        // This is a patch that should be only temporary.
        // Later on we should be able to think of a better long term solution to this problem.
        if (participationDTO.isUpdateOperation() == null) {
            tournament.addParticipation(participation);
            tournamentRepository.save(tournament);
        }

        return resultParticipation;
    }

    public List<Participation> findAll() {
        return participationRepository.findAll();
    }

    public Participation findById(Long participationId) {
        return participationRepository.findById(participationId).orElse(null);
    }

    public void deleteParticipation(Participation participation) throws ResourceNotFoundException {
        Long tournamentId = participation.getTournament().getId();
        Tournament tournament;
        if (tournamentId != null) {
            Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentId);
            if (optionalTournament.isEmpty()) {
                throw new ResourceNotFoundException("A Tournament with Id: " + tournamentId + "has not been found to be associated with this participation!");
            } else {
                tournament = optionalTournament.get();
            }
        } else {
            throw new IllegalArgumentException("Participation without a Tournament is not allowed");
        }

        tournament.removeParticipation(participation);
        tournamentRepository.save(tournament);

        participationRepository.delete(participation);
    }

}
