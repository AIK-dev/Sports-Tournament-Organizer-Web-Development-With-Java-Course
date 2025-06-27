package com.tournament_organizer.dto.team;

import com.tournament_organizer.dto.player.PlayerOutDTO;
import com.tournament_organizer.enums.AgeGroup;
import com.tournament_organizer.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class TeamOutDTO {
    private long id;
    private String name;
    private AgeGroup ageGroup;
    private TeamType type;
    private List<PlayerOutDTO> players;
}


