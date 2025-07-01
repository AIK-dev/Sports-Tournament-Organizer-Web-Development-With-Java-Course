package com.tournament_organizer.dto.venue;

import com.tournament_organizer.enums.Sport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class VenueInDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    @Positive
    private int capacity;
    @NotEmpty
    private Set<Sport> supportedSports;

}