package com.tournament_organizer.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: We can make sure to add DTOs to the ParticipationOUTDTO so that we actually display the information about the current or player
//  that is involved in the participation and also display the information about the tournament. Right now we need to first add the
//  Player and Team OUT DTOs in order to do that
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ParticipationOutDTO {
    private Long participantId;
    private Long playerId;
    private Long teamId;
    private Long tournamentId;
}
