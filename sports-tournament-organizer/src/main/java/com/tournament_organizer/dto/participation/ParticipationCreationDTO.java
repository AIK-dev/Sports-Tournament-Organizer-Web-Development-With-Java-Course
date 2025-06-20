package com.tournament_organizer.dto.participation;

public class ParticipationCreationDTO {
    private Long participantId;
    private Long playerId;
    private Long teamId;
    private Long tournamentId;
    private Boolean updateOperation;


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

    public void setUpdateOperation(Boolean updateOperation) {
        this.updateOperation = updateOperation;
    }

    public Boolean isUpdateOperation() {
        return updateOperation;
    }
}

