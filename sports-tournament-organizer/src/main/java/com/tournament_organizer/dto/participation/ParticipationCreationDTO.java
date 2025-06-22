package com.tournament_organizer.dto.participation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ParticipationCreationDTO {
    private Long playerId;
    private Long teamId;
    @NotNull
    private Long tournamentId;
}

