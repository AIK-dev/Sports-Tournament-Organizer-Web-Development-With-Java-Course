package com.tournament_organizer.service;

import com.tournament_organizer.dto.match.MatchInDTO;
import com.tournament_organizer.dto.match.MatchOutDTO;
import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.entity.Match;
import com.tournament_organizer.entity.Tournament;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.MatchMapper;
import com.tournament_organizer.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper mapper;
    private final MatchMapper matchMapper;

    @Autowired
    public MatchService(MatchRepository matchRepository, MatchMapper mapper, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.mapper = mapper;
        this.matchMapper = matchMapper;
    }

    @Transactional
    public MatchOutDTO save(MatchInDTO dto) {
        Match entity = mapper.toEntity(dto);
        boolean duplicate =
                matchRepository.existsByTournamentIdAndParticipant1IdAndParticipant2Id(entity.getTournamentId(),
                        entity.getParticipant1Id(),
                        entity.getParticipant2Id());

        if (duplicate){
            throw new IllegalStateException("Such a match already exists.");
        }
        return mapper.toDto(matchRepository.save(entity));
    }
    @Transactional
    public MatchOutDTO update(Long id, MatchInDTO patch) {
        Match entity = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match " + id + " not found"));
        mapper.updateEntity(entity, patch);
        return mapper.toDto(entity);
    }

    public List<MatchOutDTO> findAll() {
        return matchRepository.findAll().stream()
                .map(mapper::toDto).toList();
    }

    public MatchOutDTO findById(long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match " + matchId + " not found"));
        return mapper.toDto(match);
    }

    public List<MatchOutDTO> tournamentSchedule(Long tournamentId) {
        return matchRepository.findByTournamentId_Id(tournamentId).stream()
                .map(matchMapper::toDto)
                .toList();
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public void deleteById(long matchId) {
        matchRepository.deleteById(matchId);
    }
}
