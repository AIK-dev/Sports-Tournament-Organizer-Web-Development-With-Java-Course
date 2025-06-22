package com.tournament_organizer.service;

import com.tournament_organizer.entity.Player;
import com.tournament_organizer.entity.Team;
import com.tournament_organizer.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
    public Team save(Team team) {
        return teamRepository.save(team);
    }
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
    /*public Team addPlayer(Long teamId, Player player) {
        Team team = findById(teamId);
        if (team != null) {
            team.addPlayer(player);

            return save(team);
        }
        return null;
    }

    public Team removePlayer(Long teamId, Long playerId) {
        Team team = findById(teamId);
        if (team != null) {
            Player player = team.getPlayers().stream().filter(b -> b.getId().equals(playerId)).findFirst().orElse(null);
            if (player != null) {
                team.removePlayer(player);
                return save(team);
            }
        }
        return null;
    }*/
}
