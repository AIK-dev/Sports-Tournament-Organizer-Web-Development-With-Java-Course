package com.tournament_organizer.service;


import com.tournament_organizer.dto.participation.ParticipationCreationDTO;
import com.tournament_organizer.dto.participation.ParticipationDisplayDTO;
import com.tournament_organizer.entity.Participation;
import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.ParticipationMapper;
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

    /*@Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ParticipationRepository participationRepository;*/
    private final ParticipationRepository participationRepo;
    private final ParticipationMapper mapper;
    private final TournamentRepository tournamentRepo;
    @Autowired
    public ParticipationService(ParticipationRepository participationRepo, ParticipationMapper mapper, TournamentRepository tournamentRepo) {
        this.participationRepo = participationRepo;
        this.mapper = mapper;
        this.tournamentRepo = tournamentRepo;
    }

    public ParticipationDisplayDTO createParticipation(ParticipationCreationDTO participationDTO) {
        Participation entity = mapper.toEntity(participationDTO);
        boolean duplicate;
        if (entity.getPlayer() != null) {
            duplicate = participationRepo.existsByTournamentAndPlayer(
                    entity.getTournament(), entity.getPlayer());
        } else {
            duplicate = participationRepo.existsByTournamentAndTeam(
                    entity.getTournament(), entity.getTeam());
        }
        if (duplicate) {
            throw new IllegalStateException(
                    "This participant is already registered in the tournament.");
        }
        participationRepo.save(entity);
        return mapper.toDto(entity);
    }

    public List<ParticipationDisplayDTO> findAll() {
        return participationRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
    public ParticipationDisplayDTO  findById(Long participationId) {
        Participation participation = participationRepo.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation " + participationId + " not found"));
        return mapper.toDto(participation);
    }

    public ParticipationDisplayDTO updateParticipation(Long participationId,
                                                       ParticipationCreationDTO participationCreationDTO)  {

        Participation participation = participationRepo.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation " + participationId + " not found"));

        mapper.updateEntity(participation, participationCreationDTO);
        participationRepo.save(participation);
        return mapper.toDto(participation);
    }
    public void deleteParticipation(Long id) {
        Participation participation = participationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participation " + id + " not found"));
        participationRepo.delete(participation);
    }

}
