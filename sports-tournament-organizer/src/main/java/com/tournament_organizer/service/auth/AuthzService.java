package com.tournament_organizer.service.auth;

import com.tournament_organizer.exception.ResourceNotFoundException;
import com.tournament_organizer.repository.ParticipationRepository;
import com.tournament_organizer.repository.PlayerRepository;
import com.tournament_organizer.repository.TeamRepository;
import com.tournament_organizer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
@RequiredArgsConstructor
public class AuthzService {

    private final TeamRepository teamRepo;
    private final PlayerRepository playerRepo;
    private final ParticipationRepository participationRepo;
    private final UserRepository userRepo;

    public boolean isTeamOwner(Long teamId) {
        String username = currentUser();
        return teamRepo.findById(teamId)
                .map(t -> t.getOwner().getUsername().equals(username))
                .orElse(false);
    }
    public boolean isPlayerOwner(Long playerId) {
        String username = currentUser();
        return playerRepo.findById(playerId)
                .map(p -> p.getOwner().getUsername().equals(username))
                .orElse(false);
    }
    public boolean isPlayerSelf(Long playerId) {
        String username = currentUser();
        return playerRepo.findById(playerId)
                .map(p -> p.getUser()!=null && p.getUser().getUsername().equals(username))
                .orElse(false);
    }
    public boolean isTeamOwnerFromPlayer(Long playerId) {
        return playerRepo.findById(playerId)
                .map(p -> p.getTeam()!=null && isTeamOwner(p.getTeam().getId()))
                .orElse(false);
    }
    public boolean isParticipationOwner(Long participationId) {
        return participationRepo.findById(participationId)
                .map(p -> {
                    if (p.getTeam() != null)
                        return isTeamOwner(p.getTeam().getId());
                    if (p.getPlayer() != null)
                        return isPlayerOwner(p.getPlayer().getId());
                    return false;
                })
                .orElse(false);
    }
    public boolean isCurrentUser(Integer userId){
        String currentUsername = currentUser();
        return userRepo.findById(userId).
                map(u -> u.getUsername().equals(currentUsername))
                .orElse(false);
    }
    private String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
