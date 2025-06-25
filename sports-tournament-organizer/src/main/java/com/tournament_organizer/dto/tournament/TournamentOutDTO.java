package com.tournament_organizer.dto.tournament;

import com.tournament_organizer.dto.participation.ParticipationOutDTO;
import com.tournament_organizer.dto.venue.VenueOutDTO;
import com.tournament_organizer.enums.DrawType;
import com.tournament_organizer.enums.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class TournamentOutDTO {
    private Long id;
    private String name;
    private Sport sport;
    private int participationLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DrawType drawType;
    private List<VenueOutDTO> venueIds;
    private List<ParticipationOutDTO> participations;
    private String rules;
}
