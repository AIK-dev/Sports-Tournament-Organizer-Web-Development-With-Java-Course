package com.tournament_organizer.dto.tournament;

import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.enums.Sport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class TournamentInDTO {
    @NotBlank
    private String name;
    @NotNull
    private Sport sport;
    @Positive
    private int participationLimit;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private DrawType drawType;
    @Size(max = 10)
    private List<Long> venueIds;
    @NotBlank
    private String rules;
}
