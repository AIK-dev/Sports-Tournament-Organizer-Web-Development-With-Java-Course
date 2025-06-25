package com.tournament_organizer.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueInDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    @Positive
    private int capacity;
}
