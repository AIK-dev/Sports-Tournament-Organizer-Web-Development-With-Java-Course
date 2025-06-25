package com.tournament_organizer.service;

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

    public Team update(Long id, Team teamDetails)  {
        Team team = findById(id);
        team.setName(teamDetails.getName());
        team.setType(teamDetails.getType());
        team.setAgeGroup(teamDetails.getAgeGroup());
        // TODO: We are going to need to also add an update operation here on Teams which allows us to update the team roster with a supplied list of user ids
        //  But this is to be done when we add Input DTOs for Teams.
        return team;
    }

    public Team findById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

}
