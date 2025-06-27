package com.tournament_organizer.dto.player;

import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.Gender;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInDTO {

    @NotBlank(message = "First name of the player cannot be blank")
    private String firstName;

    @NotBlank(message = "Second name of player cannot be blank")
    private String secondName;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Level is required")
    private AgeGroup level;

    private Long teamId;
}

