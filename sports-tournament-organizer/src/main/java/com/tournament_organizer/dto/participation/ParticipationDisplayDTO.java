package com.tournament_organizer.dto.participation;

// Note: This needs to be finished. We need a separate DTO for Displaying the Participation when sending back responses to the
// user.
public class ParticipationDisplayDTO {
    private Long participantId;
    private Long playerId;
    private Long teamId;
    private Long tournamentId;


    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getParticipantId() {
        return participantId;
    }
    public Long getPlayerId() {
        return playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }
}
