package com.tournament_organizer.dto.match;

import com.tournament_organizer.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MatchOutDTO {
    private Long id;
    private Long tournamentId;
    private Long participant1Id;
    private Long participant2Id;
    private Long venueId;
    private LocalDateTime scheduledStart;
    private Result result;
}
