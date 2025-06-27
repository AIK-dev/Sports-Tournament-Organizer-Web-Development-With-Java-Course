package com.tournament_organizer.service;

import com.tournament_organizer.dto.team.TeamInDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.TeamMapper;
import com.tournament_organizer.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Transactional
    public TeamOutDTO save(TeamInDTO dto) {
        Team team = teamMapper.toEntity(dto);
        return teamMapper.toDto(teamRepository.save(team));
    }

    @Transactional
    public TeamOutDTO update(Long id, TeamInDTO patch) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + id + " not found"));
        teamMapper.updateEntity(team, patch);
        return teamMapper.toDto(team);
    }

    public List<TeamOutDTO> findAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamOutDTO findById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + id + " not found"));
        return teamMapper.toDto(team);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}

