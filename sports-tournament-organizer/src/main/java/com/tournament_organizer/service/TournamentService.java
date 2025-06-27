package com.tournament_organizer.service;

import com.tournament_organizer.dto.tournament.TournamentInDTO;
import com.tournament_organizer.dto.tournament.TournamentOutDTO;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.TournamentMapper;
import com.tournament_organizer.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper mapper;
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper mapper) {
        this.tournamentRepository = tournamentRepository;
        this.mapper = mapper;
    }

    public TournamentOutDTO create(TournamentInDTO dto) {
        Tournament entity = mapper.toEntity(dto);
        if (entity.getParticipationLimit() <= 0) {
            throw new IllegalStateException("Participation limit must be positive");
        }
        return mapper.toDto(tournamentRepository.save(entity));
    }

    public List<TournamentOutDTO> findAll() {
        return tournamentRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public TournamentOutDTO  findById(long tournamentId) {
        return mapper.toDto(
                tournamentRepository.findById(tournamentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Tournament " + tournamentId + " not found"))
        );
    }
    @Transactional
    public TournamentOutDTO update(Long id, TournamentInDTO patch) {
        Tournament entity = tournamentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tournament " + id + " not found"));
        mapper.updateEntity(entity, patch);
        return mapper.toDto(tournamentRepository.save(entity));
    }

    public void delete(Tournament tournament) {
        tournamentRepository.delete(tournament);
    }

    public void deleteById(long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Tournament " + tournamentId + " not found");
        }
        tournamentRepository.deleteById(tournamentId);
    }
}
