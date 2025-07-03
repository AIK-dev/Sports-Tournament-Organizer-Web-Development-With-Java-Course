package com.tournament_organizer.service;

import com.tournament_organizer.dto.team.TeamInDTO;
import com.tournament_organizer.dto.team.TeamOutDTO;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.enums.Role;
import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.mappers.TeamMapper;
import com.tournament_organizer.repository.TeamRepository;
import com.tournament_organizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userRepository = userRepository;
    }
    @Transactional
    public TeamOutDTO save(TeamInDTO dto) {
        Team team = teamMapper.toEntity(dto);
        team.setOwner(currentUser());
        return teamMapper.toDto(teamRepository.save(team));
    }
    public List<TeamOutDTO> findAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamOutDTO update(Long id, TeamInDTO  patch)  {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + id + " not found"));
        teamMapper.updateEntity(team, patch);
        return teamMapper.toDto(team);
    }

    public TeamOutDTO  findById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + id + " not found"));
        return teamMapper.toDto(team);    }
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
    @Transactional
    public TeamOutDTO reassignOwner(Long teamId, Integer newOwnerId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team " + teamId));
        User newOwner = userRepository.findById(newOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + newOwnerId));
        if (newOwner.getRole() != Role.ORGANIZER && newOwner.getRole() != Role.ADMIN)
            throw new IllegalStateException("New owner must be ORGANIZER or ADMIN");
        team.setOwner(newOwner);
        return teamMapper.toDto(team);
    }
    private User currentUser() {
        String uname = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(uname).orElseThrow();
    }
}
