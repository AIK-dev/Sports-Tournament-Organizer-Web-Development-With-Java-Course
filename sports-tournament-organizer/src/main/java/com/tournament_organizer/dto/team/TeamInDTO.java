package com.tournament_organizer.dto.team;

import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamInDTO {

    @NotBlank
    private String name;

    @NotNull
    private AgeGroup ageGroup;

    @NotNull
    private TeamType teamType;

    // Note: Not mandatory, but we should probably include the option for adding a list of players to the team from the stage of
    // creation of the team. If we decide to supply a list of Ids for players we can do that as well.
    private List<Long> playerIds;
}

