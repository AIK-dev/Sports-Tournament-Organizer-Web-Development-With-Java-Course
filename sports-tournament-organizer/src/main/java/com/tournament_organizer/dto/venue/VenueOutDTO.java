package com.tournament_organizer.dto.venue;

import com.tournament_organizer.enums.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class VenueOutDTO {
    private long id;
    private String name;
    private String city;
    private int capacity;
    private Set<Sport> supportedSports;
}