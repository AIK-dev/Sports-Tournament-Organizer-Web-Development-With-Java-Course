package com.tournament_organizer.dto.match;

import com.tournament_organizer.enums.Result;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchInDTO {
    @NotNull
    private Long  tournamentId;
    @NotNull
    private Long  participant1Id;
    @NotNull
    private Long  participant2Id;
    private Long  venueId;
    @NotNull
    private LocalDateTime scheduledStart;
    private Result result;
}
