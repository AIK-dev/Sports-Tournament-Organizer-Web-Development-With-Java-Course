package com.tournament_organizer.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Note: This needs to be finished. We need a separate DTO for Displaying the Participation when sending back responses to the
// user.
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ParticipationDisplayDTO {
    private Long participantId;
    private Long playerId;
    private Long teamId;
    private Long tournamentId;

}
