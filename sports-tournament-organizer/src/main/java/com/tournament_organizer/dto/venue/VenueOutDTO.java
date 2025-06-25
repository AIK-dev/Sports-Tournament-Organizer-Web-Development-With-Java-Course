package com.tournament_organizer.dto.venue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VenueOutDTO {
    private long id;
    private String name;
    private String city;
    private int capacity;
}
