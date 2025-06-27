package com.tournament_organizer.dto.participation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationInDTO {
    private Long playerId;
    private Long teamId;
    @NotNull
    private Long tournamentId;
}

